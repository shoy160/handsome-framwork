package cn.handsome.sap.manage;

import cn.handsome.core.utils.CollectionUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.sap.SapRfcException;
import cn.handsome.sap.dto.SapRfcCredentials;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author luoyong
 * @date 2025/5/7
 */
@Slf4j
public class SapRfcHelper {
    private final static String CFG_EXTENSION = ".jcoDestination";
    private final static SimpleDateFormat SAP_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private final static SimpleDateFormat SAP_TIME_FORMAT = new SimpleDateFormat("HHmmss");
    private final String name;
    private final String cfgPath;
    private final SapRfcCredentials credentials;

    public SapRfcHelper(String name, SapRfcCredentials credentials) {
        this.name = name;
        this.cfgPath = name + CFG_EXTENSION;
        this.credentials = credentials;
        createDataFile();
    }

    public void verify(String token) throws SapRfcException {
        String clientId = credentials.getClientId();
        String clientSecret = credentials.getClientSecret();
        if (StrUtil.isBlank(clientId) && StrUtil.isBlank(clientSecret)) {
            return;
        }
        String correctToken = String.format("Basic %s", cn.hutool.core.codec.Base64.encode(String.format("%s:%s", clientId, credentials.getClientSecret())));
        if (!StrUtil.equalsIgnoreCase(correctToken, token)) {
            throw new SapRfcException("SAP 客户端鉴权失败");
        }
    }

    private synchronized void createDataFile() {
        File cfg = new File(name + CFG_EXTENSION);
        if (cfg.exists()) {
            return;
        }
        Properties connectProperties = new Properties();
        //服务器
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, credentials.getHost());
//        // 端口号
//        connectProperties.setProperty(DestinationDataProvider.JCO_MSSERV, credentials.getPort());
//        //服务器
//        connectProperties.setProperty(DestinationDataProvider.JCO_GWHOST, credentials.getHost());
//        // 端口号
//        connectProperties.setProperty(DestinationDataProvider.JCO_GWSERV, credentials.getPort());
        //系统编号
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, credentials.getSystemNumber());
        //SAP集团
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, credentials.getClient());
        String group = credentials.getGroup();
        if (StrUtil.isNotBlank(group)) {
            connectProperties.setProperty(DestinationDataProvider.JCO_GROUP, group);
        }
        //SAP用户名
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, credentials.getUsername());
        //密码
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, credentials.getPassword());
        //登录语言
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, credentials.getLang());
        if (Objects.nonNull(credentials.getPoolCapacity())) {
            //最大连接数
            connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, credentials.getPoolCapacity().toString());
        }
        if (Objects.nonNull(credentials.getPeakLimit())) {
            //最大连接线程
            connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, credentials.getPeakLimit().toString());
        }
        createDataFile(connectProperties);
    }

    /**
     * 创建SAP接口属性文件。
     *
     * @param properties 属性文件内容
     */
    private void createDataFile(Properties properties) {
        File cfg = new File(cfgPath);
        if (cfg.exists()) {
            cfg.deleteOnExit();
        }
        try {
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "Jco连接配置信息");
            fos.close();
        } catch (Exception e) {
            log.error("创建Jco连接属性文件失败, 错误信息: " + e);
            throw new SapRfcException("不能创建连接属性文件： " + cfg.getName(), e);
        }
    }

    private void deleteDataFile() {
        File cfg = new File(cfgPath);
        if (cfg.exists()) {
            boolean delete = cfg.delete();
            log.info("{} is deleted: {}", name, delete);
        }
    }

    /**
     * 获取SAP连接
     *
     * @return SAP连接对象
     */
    public JCoDestination connect() {
        try {
            JCoDestination destination = JCoDestinationManager.getDestination(name);
            destination.ping();
            return destination;
        } catch (Throwable e) {
            deleteDataFile();
            throw new SapRfcException(String.format("连接 SAP 失败, 错误信息: %s", e.getMessage()));
        }
    }

    public Map<String, Object> invokeFunc(String func, Map<String, Object> params, boolean errorCheck) {
        JCoDestination destination = connect();
        try {
            JCoFunction jCoFunction = destination.getRepository().getFunction(func);
            if (MapUtil.isNotEmpty(params)) {
                setInputParameters(jCoFunction, params);
            }
            jCoFunction.execute(destination);
            return convertExportParametersToMap(jCoFunction, errorCheck);
        } catch (Exception e) {
            if (e instanceof SapRfcException) {
                throw (SapRfcException) e;
            }
            throw new SapRfcException(String.format("调用 SAP 函数 %s 异常：%s", func, e.getMessage()));
        }
    }

    /**
     * 将RFC输出参数转换为Map
     */
    private static Map<String, Object> convertExportParametersToMap(JCoFunction function, boolean errorCheck) throws SapRfcException {
        Map<String, Object> exportParams = convertParametersToMap(function.getExportParameterList(), errorCheck);
        Map<String, Object> tableParams = convertParametersToMap(function.getTableParameterList(), errorCheck);
        Map<String, Object> result = new HashMap<>(2);
        result.put("tableParams", tableParams);
        result.put("exportParams", exportParams);
        return result;
    }

    /**
     * 将RFC输出参数转换为Map
     */
    private static Map<String, Object> convertParametersToMap(JCoParameterList parameterList, boolean errorCheck) throws SapRfcException {
        if (Objects.isNull(parameterList)) {
            return new HashMap<>(0);
        }
        Map<String, Object> resultMap = new HashMap<>();

        try {
            // 处理输出参数
            JCoParameterFieldIterator fieldIterator = parameterList.getParameterFieldIterator();
            while (fieldIterator.hasNextField()) {
                JCoField jCoField = fieldIterator.nextField();
                if (jCoField.isStructure()) {
                    // 返回结果异常处理
                    JCoStructure structure = jCoField.getStructure();
                    resultMap.put(jCoField.getName(), convertStructureToMap(structure));
                } else if (jCoField.isTable()) {
                    // 处理表类型
                    JCoTable table = jCoField.getTable();
                    resultMap.put(jCoField.getName(), convertTableToListMap(table));
                } else {
                    // 处理基本类型
                    Object value = convertSapValueToJava(jCoField);
                    resultMap.put(jCoField.getName(), value);
                }
            }
            resultCheck(resultMap, errorCheck);
            return resultMap;
        } catch (Exception e) {
            if (e instanceof SapRfcException) {
                throw (SapRfcException) e;
            }
            throw new SapRfcException("转换输出参数失败: " + e.getMessage(), e);
        }
    }

    private static void resultCheck(Map<String, Object> result, boolean errorCheck) {
        if (!errorCheck) {
            return;
        }
        Object returnValue = result.get("RETURN");
        if (Objects.isNull(returnValue)) {
            return;
        }
        List<Map<String, Object>> list = CollectionUtils.convertToMapList(returnValue);
        List<String> errors = new ArrayList<>(list.size());
        for (Map<String, Object> item : list) {
            String type = MapUtil.getStr(item, "TYPE");
            String message = MapUtil.getStr(item, "MESSAGE");
            String[] errorTypes = {"E", "A"};
            if (ArrayUtil.contains(errorTypes, type)) {
                errors.add(message);
            }
        }
        if (CollUtil.isNotEmpty(errors)) {
            throw new SapRfcException(String.format("SAP 调用返回异常: %s", String.join(", ", errors)));
        }
    }

    /**
     * 设置RFC函数输入参数
     */
    private static void setInputParameters(JCoFunction function, Map<String, Object> inputParams) throws SapRfcException {
        JCoParameterList importParams = function.getImportParameterList();
        if (Objects.isNull(importParams)) {
            return;
        }
        List<String> inputFields = new ArrayList<>();
        importParams.forEach(param -> {
            inputFields.add(param.getName());
        });
        log.info("function {} input parameters: {}", function.getName(), String.join(",", inputFields));

        try {
            JCoParameterFieldIterator fieldIterator = importParams.getParameterFieldIterator();
            while (fieldIterator.hasNextField()) {
                JCoField jCoField = fieldIterator.nextField();
                String paramName = jCoField.getName();
                Object value = inputParams.get(paramName);
                if (Objects.isNull(value)) {
                    continue;
                }
                if (jCoField.isStructure()) {
                    // 处理结构类型输入参数
                    if (!(value instanceof Map)) {
                        throw new SapRfcException("参数 " + paramName + " 应为 Map 类型");
                    }
                    JCoStructure structure = importParams.getStructure(paramName);
                    setStructureValues(structure, MapUtils.map(value));
                } else if (jCoField.isTable()) {
                    // 处理表类型输入参数
                    if (!(value instanceof List)) {
                        throw new SapRfcException("参数 " + paramName + " 应为 List 类型");
                    }
                    JCoTable table = importParams.getTable(paramName);
                    setTableValues(table, CollectionUtils.convertToMapList(value));
                } else {
                    // 处理基本类型输入参数
                    setBasicParameterValue(importParams, paramName, value);
                }
            }
        } catch (JCoException e) {
            throw new SapRfcException("设置输入参数失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将SAP结构转换为Map
     */
    private static Map<String, Object> convertStructureToMap(JCoStructure structure) throws SapRfcException {
        if (Objects.isNull(structure)) {
            return new HashMap<>(0);
        }
        Map<String, Object> structMap = new HashMap<>(structure.getFieldCount());
        JCoFieldIterator fieldIterator = structure.getFieldIterator();
        while (fieldIterator.hasNextField()) {
            JCoField jCoField = fieldIterator.nextField();
            Object value = convertSapValueToJava(jCoField);
            structMap.put(jCoField.getName(), value);
        }
        return structMap;
    }

    /**
     * 将SAP内表转换为List<Map>
     */
    private static List<Map<String, Object>> convertTableToListMap(JCoTable table) throws SapRfcException {
        if (table.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<Map<String, Object>> tableList = new ArrayList<>();
        table.firstRow();
        do {
            Map<String, Object> rowMap = new HashMap<>(table.getRow());
            JCoFieldIterator fieldIterator = table.getFieldIterator();
            while (fieldIterator.hasNextField()) {
                JCoField jCoField = fieldIterator.nextField();
                Object value = convertSapValueToJava(jCoField);
                rowMap.put(jCoField.getName(), value);
            }
            tableList.add(rowMap);
        } while (table.nextRow());

        return tableList;
    }

    /**
     * 将SAP数据类型转换为Java数据类型，处理各种可能的类型转换异常
     */
    private static Object convertSapValueToJava(JCoField field) throws SapRfcException {
        if (Objects.isNull(field)) {
            return null;
        }
        int type = field.getType();
        String fieldName = field.getName();
        Object value = field.getValue();
        // 处理空值
        if (Objects.isNull(value)) {
            return null;
        }
        // 根据SAP数据类型进行转换
        switch (type) {
            case JCoMetaData.TYPE_DATE:
                // 处理日期类型
                if (value instanceof String) {
                    String dateStr = (String) value;
                    if (dateStr.trim().isEmpty()) {
                        return null;
                    }
                    try {
                        return SAP_DATE_FORMAT.parse(dateStr);
                    } catch (ParseException e) {
                        throw new SapRfcException("字段 " + fieldName + " 日期格式错误: " + dateStr, e);
                    }
                }
                break;

            case JCoMetaData.TYPE_TIME:
                // 处理时间类型
                if (value instanceof String) {
                    String timeStr = (String) value;
                    if (timeStr.trim().isEmpty()) {
                        return null;
                    }
                    try {
                        return SAP_TIME_FORMAT.parse(timeStr);
                    } catch (ParseException e) {
                        throw new SapRfcException("字段 " + fieldName + " 时间格式错误: " + timeStr, e);
                    }
                }
                break;

            case JCoMetaData.TYPE_NUM:
            case JCoMetaData.TYPE_INT:
            case JCoMetaData.TYPE_INT1:
            case JCoMetaData.TYPE_INT2:
                // 处理数字类型
                if (value instanceof String) {
                    try {
                        // 尝试转换为Long，如果太大则转为BigDecimal
                        return Long.parseLong((String) value);
                    } catch (NumberFormatException e) {
                        return new java.math.BigDecimal((String) value);
                    }
                }
                break;

            case JCoMetaData.TYPE_FLOAT:
            case JCoMetaData.TYPE_BCD:
                // 处理浮点和BCD类型
                if (value instanceof String) {
                    try {
                        return new java.math.BigDecimal((String) value);
                    } catch (NumberFormatException e) {
                        throw new SapRfcException("字段 " + fieldName + " 数字格式错误: " + value, e);
                    }
                }
                break;

            case JCoMetaData.TYPE_STRING:
            case JCoMetaData.TYPE_CHAR:
                // 处理字符串类型，去除多余空格
                if (value instanceof String) {
                    return ((String) value).trim();
                }
                break;

            case JCoMetaData.TYPE_BYTE:
                // 处理字节类型，转换为Base64字符串
                if (value instanceof byte[]) {
                    return Base64.getEncoder().encodeToString((byte[]) value);
                }
                break;
            default:
                break;
        }

        // 对于未处理的类型，直接返回原始值
        return value;
    }

    /**
     * 设置结构字段值
     */
    private static void setStructureValues(JCoStructure structure, Map<String, Object> values) throws JCoException, SapRfcException {
        JCoFieldIterator fieldIterator = structure.getFieldIterator();
        while (fieldIterator.hasNextField()) {
            JCoField jCoField = fieldIterator.nextField();
            String fieldName = jCoField.getName();
            Object value = values.get(fieldName);
            if (Objects.isNull(value)) {
                continue;
            }
            setFieldValue(structure, fieldName, value);
        }
    }

    /**
     * 设置内表值
     */
    private static void setTableValues(JCoTable table, List<Map<String, Object>> rows) throws JCoException, SapRfcException {
        table.deleteAllRows();

        for (Map<String, Object> row : rows) {
            table.appendRow();
            JCoRecordFieldIterator fieldIterator = table.getRecordFieldIterator();
            while (fieldIterator.hasNextField()) {
                JCoField jCoField = fieldIterator.nextField();
                String fieldName = jCoField.getName();
                Object value = row.get(fieldName);
                setFieldValue(table, fieldName, value);
            }
        }
    }

    /**
     * 设置基本参数值
     */
    private static void setBasicParameterValue(JCoParameterList paramList, String paramName, Object value)
            throws JCoException, SapRfcException {
        JCoParameterFieldIterator fieldIterator = paramList.getParameterFieldIterator();
        boolean hasField = false;
        while (fieldIterator.hasNextField()) {
            JCoField jCoField = fieldIterator.nextField();
            String fieldName = jCoField.getName();
            if (StrUtil.equalsIgnoreCase(fieldName, paramName)) {
                hasField = true;
                break;
            }
        }
        if (!hasField) {
            throw new SapRfcException("参数列表中不存在参数 " + paramName);
        }

        setFieldValue(paramList, paramName, value);
    }

    /**
     * 设置字段值，处理Java类型到SAP类型的转换
     */
    private static void setFieldValue(JCoRecord record, String fieldName, Object value)
            throws SapRfcException {
        if (value == null) {
            record.setValue(fieldName, "");
            return;
        }

        int type = record.getMetaData().getType(fieldName);

        try {
            switch (type) {
                case JCoMetaData.TYPE_DATE:
                    // 处理日期类型
                    if (value instanceof Date) {
                        record.setValue(fieldName, SAP_DATE_FORMAT.format((Date) value));
                    } else if (value instanceof String) {
                        // 尝试解析字符串为日期
                        Date date = SAP_DATE_FORMAT.parse((String) value);
                        record.setValue(fieldName, SAP_DATE_FORMAT.format(date));
                    } else {
                        throw new SapRfcException("字段 " + fieldName + " 需为日期类型");
                    }
                    break;

                case JCoMetaData.TYPE_TIME:
                    // 处理时间类型
                    if (value instanceof Date) {
                        record.setValue(fieldName, SAP_TIME_FORMAT.format((Date) value));
                    } else if (value instanceof String) {
                        Date time = SAP_TIME_FORMAT.parse((String) value);
                        record.setValue(fieldName, SAP_TIME_FORMAT.format(time));
                    } else {
                        throw new SapRfcException("字段 " + fieldName + " 需为时间类型");
                    }
                    break;

                case JCoMetaData.TYPE_NUM:
                case JCoMetaData.TYPE_INT:
                case JCoMetaData.TYPE_INT1:
                case JCoMetaData.TYPE_INT2:
                case JCoMetaData.TYPE_FLOAT:
                case JCoMetaData.TYPE_BCD:
                    // 处理数字类型
                    record.setValue(fieldName, value.toString());
                    break;

                case JCoMetaData.TYPE_STRING:
                case JCoMetaData.TYPE_CHAR:
                    // 处理字符串类型
                    record.setValue(fieldName, value.toString());
                    break;

                case JCoMetaData.TYPE_BYTE:
                    // 处理字节类型
                    if (value instanceof String) {
                        // 假设是Base64编码的字符串
                        byte[] bytes = Base64.getDecoder().decode((String) value);
                        record.setValue(fieldName, bytes);
                    } else if (value instanceof byte[]) {
                        record.setValue(fieldName, value);
                    } else {
                        throw new SapRfcException("字段 " + fieldName + " 需为字节数组或Base64字符串");
                    }
                    break;

                default:
                    // 对于其他类型，尝试直接设置
                    record.setValue(fieldName, value.toString());
            }
        } catch (ParseException e) {
            throw new SapRfcException("字段 " + fieldName + " 格式转换失败", e);
        }
    }
}
