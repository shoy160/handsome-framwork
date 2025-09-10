package cn.handsome.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.XmlUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * xml util
 *
 * @author shay
 * @date 2021/3/26
 */
public class XmlUtils {
    public static <T> T deserialize(String xml, Class<T> clazz) {
        Map<String, Object> map = XmlUtil.xmlToMap(xml);
        CopyOptions options = new CopyOptions();
        Map<String, String> fieldMapping = CommonUtils.getFieldMapping(clazz, true);
        options.setFieldMapping(fieldMapping);
        return BeanUtil.mapToBean(map, clazz, options);
    }

    public static String serialize(Object obj) {
        if (obj == null) {
            return "";
        }
        Map<String, String> fieldMapping = CommonUtils.getFieldMapping(obj.getClass(), false);
        Map<String, Object> map = BeanUtil.beanToMap(obj, new LinkedHashMap(), false, key -> fieldMapping.getOrDefault(key, key));
        String rootName = CommonUtils.getName(obj.getClass());
        return XmlUtil.mapToXmlStr(map, rootName);
    }
}
