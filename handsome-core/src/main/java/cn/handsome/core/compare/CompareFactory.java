package cn.handsome.core.compare;

import cn.handsome.core.Constants;
import cn.handsome.core.Singleton;
import cn.handsome.core.compare.enums.ConditionConjunction;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.handsome.core.compare.model.CompareCondition;
import cn.handsome.core.compare.model.CompareConditions;
import cn.handsome.core.compare.model.ValueChangedDTO;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.core.utils.ReflectUtils;
import cn.handsome.core.utils.TypeUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 数据过滤器
 *
 * @author luoyong
 * @date 2023/6/25
 */
@Slf4j
@Component
public class CompareFactory {
    private final Map<ConditionOp, ICompare> compareMap;
    private final ScriptEngine scriptEngine;

    private CompareFactory() {
        compareMap = new HashMap<>();
        Set<Class<?>> classes = ReflectUtils.findClasses(
                clazz -> TypeUtils.isAssignableFrom(clazz, ICompare.class)
        );
        for (Class<?> clazz : classes) {
            ICompare instance = (ICompare) ReflectUtil.newInstance(clazz);
            compareMap.put(instance.getOperation(), instance);
        }
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        scriptEngine = factory.getEngineByName("JavaScript");
    }

    public static CompareFactory getInstance() {
        return Singleton.instance(CompareFactory.class);
    }

    /**
     * 是否匹配
     */
    public boolean isMatch(
            CompareConditions filterCondition, Map<String, Object> item, Map<String, Object> context
    ) {

        ConditionConjunction conjunction = filterCondition.getConjunction();
        List<CompareCondition> conditions = filterCondition.getConditions();

        boolean result = false;

        // 判断 conditions
        for (CompareCondition condition : conditions) {
            String key = condition.getKey();
            if (StrUtil.isBlank(key)) {
                continue;
            }

            Object value = resolveValue(item, context, key);
            // $item 当前实体，$ 上下文
            Object valueToCompare = resolveContext(item, context, condition.getValue());
            boolean isMatch = isMatch(condition, value, valueToCompare);
            if (filterCondition.isEnableLog()) {
                log.debug("「条件匹配」{} [{}] {} {}：{}", condition.getKey(), value, condition.getOp(), valueToCompare, isMatch);
            }
            if (conjunction == ConditionConjunction.and) {
                // and 条件，有一个不匹配，返回 false
                if (!isMatch) {
                    return false;
                } else {
                    result = true;
                }
            } else if (conjunction == ConditionConjunction.or) {
                // or 条件，有一个匹配，返回 true
                if (isMatch) {
                    return true;
                }
            }
        }

        List<CompareConditions> conditionGroups = filterCondition.getGroups();
        // 判断 groups
        for (CompareConditions condition : conditionGroups) {
            boolean isMatch = isMatch(condition, item, context);
            if (conjunction == ConditionConjunction.and) {
                // and 条件，有一个不匹配，返回 false
                if (!isMatch) {
                    return false;
                }
                result = true;
            } else if (conjunction == ConditionConjunction.or) {
                // or 条件，有一个匹配，返回 true
                if (isMatch) {
                    return true;
                }
            }
        }
        return result;
    }

    /**
     * 单条件匹配
     */
    public boolean isMatch(
            CompareCondition condition, Object value, Object valueToCompare
    ) {
        ICompare compare = getCompare(condition);
        if (Objects.nonNull(compare)) {
            String dataType = condition.getDataType();
            switch (dataType) {
                case "date":
                case "datetime":
                    value = toDate(value);
                    valueToCompare = toDate(valueToCompare);
                    break;
                default:
                    break;
            }
            return compare.compare(value, valueToCompare);
        }
        return false;
    }

    private Date toDate(Object value) {
        Date date;
        if (value instanceof Integer) {
            date = new Date((Integer) value * 1000L);
        } else if (value instanceof Long) {
            date = new Date((Long) value);
        } else {
            date = Convert.toDate(value);
        }
        return date;
    }

    private ICompare getCompare(CompareCondition condition) {
        if (Objects.isNull(condition)) {
            return null;
        }
        ConditionOp op = condition.getOp();
        if (Objects.equals(ConditionOp.RECENT, op)) {
            if (Objects.equals("datetime", condition.getDataType())) {
                return compareMap.get(ConditionOp.RECENT_TIME);
            } else if (Objects.equals("date", condition.getDataType())) {
                return compareMap.get(ConditionOp.RECENT_DATE);
            }
        }
        return compareMap.get(op);
    }

    private Object resolveValue(
            Map<String, Object> data, Map<String, Object> context, String key
    ) {
        if (key.contains(Constants.DOLLAR)) {
            return evalScript(data, context, key);
        }
        return MapUtils.get(data, key);
    }

    private Object resolveContext(Map<String, Object> data, Map<String, Object> context, Object value) {
        if (TypeUtils.isString(value)) {
            String key = value.toString();
            Object result = resolveContext(data, context, key);
            if (Objects.nonNull(result)) {
                return result;
            }
        }
        return value;
    }

    private Object resolveContext(
            Map<String, Object> data, Map<String, Object> context, String expression
    ) {
        if (ReUtil.isMatch("^\\$[a-z0-9A-Z_]+$", expression)) {
            String variableName = expression.substring(1);
            Object contextValue = context.get(variableName);
            if (Objects.nonNull(contextValue)) {
                return contextValue;
            }
        }
        if (expression.contains(Constants.DOLLAR)) {
            return evalScript(data, context, expression);
        }
        return null;
    }

    private synchronized Object evalScript(
            Map<String, Object> data, Map<String, Object> context, String expression
    ) {
        // evaluate JavaScript code from String
        try {
            scriptEngine.eval(String.format("function resolveValue($item, $){ return %s }", expression));
            return ((Invocable) scriptEngine).invokeFunction("resolveValue", data, context);
        } catch (Exception ex) {
            log.error("解析表达式失败", ex);
            throw new RuntimeException(String.format("解析表达式失败：item=%s, expression=%s", JsonUtils.toJson(data), expression));
        }
    }

    public static Map<String, ValueChangedDTO> changedFields(
            Object source, Object target
    ) {
        return changedFields(source, target, new ArrayList<>(0), true, null);
    }

    public static Map<String, ValueChangedDTO> changedFields(
            Object source, Object target, List<String> emptyCompareFields,
            boolean ignoreNull, List<String> ignoreFields
    ) {
        Map<String, ValueChangedDTO> changedFields = new HashMap<>(0);
        Map<String, Object> sourceMap = MapUtils.map(source);
        Map<String, Object> targetMap = MapUtils.map(target);
        if (MapUtil.isEmpty(sourceMap) || MapUtil.isEmpty(targetMap)) {
            return changedFields;
        }
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = entry.getKey();
            Object sourceValue = entry.getValue();
            // 空值对比处理
            if (!emptyCompareFields.contains(key) && ignoreNull && ObjUtil.isEmpty(sourceValue)) {
                continue;
            }
            if (CollUtil.isNotEmpty(ignoreFields) && ignoreFields.contains(key)) {
                continue;
            }
            Object targetValue = targetMap.get(key);
            String sourceStr;
            String targetStr;
            sourceStr = objToString(sourceValue);
            targetStr = objToString(targetValue);
            // 手机号处理前缀区号处理
//            if (isMobileField(key) && phoneMatch(sourceStr, targetStr)) {
//                continue;
//            }
            // todo 层级
            if (isFieldChanged(sourceStr, targetStr)) {
                changedFields.put(key, new ValueChangedDTO(targetValue, sourceValue));
            }
        }
        return changedFields;
    }

    public static boolean isFieldChanged(String source, String target) {
        if (isAllEmpty(source, target)) {
            return false;
        }
        try {
            // 实体或列表对比
            if (isObjectJson(target)) {
                if (!isObjectJson(source)) {
                    return true;
                }
                Map<String, Object> sourceMap = JsonUtils.jsonMap(source);
                Map<String, Object> targetMap = JsonUtils.jsonMap(target);
                return !mapEquals(sourceMap, targetMap);
            }
            if (isArrayJson(target)) {
                if (!isArrayJson(source)) {
                    return true;
                }
                List<Object> sourceList = JsonUtils.jsonList(source, Object.class);
                List<Object> targetList = JsonUtils.jsonList(target, Object.class);
                return sourceList.size() != targetList.size()
                        || sourceList.stream().anyMatch(s -> targetList.stream().noneMatch(t -> objectEquals(s, t)));
            }
        } catch (Exception ignored) {
        }
        return !objectEquals(source, target);
    }

    public static boolean objectEquals(Object source, Object target) {
        if (Objects.isNull(source)) {
            return Objects.isNull(target);
        }
        if (TypeUtils.isInt(source)) {
            return Objects.equals(source, Convert.toInt(target));
        }
        if (TypeUtils.isString(source)) {
            return Objects.equals(source, String.valueOf(target));
        }
        return ObjUtil.equal(source, target);
    }

    public static boolean mapEquals(Map<?, ?> source, Map<?, ?> target) {
        if (MapUtil.isEmpty(source)) {
            return MapUtil.isEmpty(target);
        }
        if (MapUtil.isEmpty(target) || !Objects.equals(source.size(), target.size())) {
            return false;
        }
        return source.entrySet().stream()
                .allMatch(t -> Objects.equals(t.getValue(), target.get(t.getKey())));
    }

    public static boolean isAllEmpty(CharSequence... values) {
        if (ArrayUtil.isEmpty(values)) {
            return true;
        }
        final CharSequence[] emptyObject = {null, "", "{}"};
        final CharSequence[] emptyArray = {null, "", "[]"};
        return ArrayUtil.containsAll(emptyObject, values) || ArrayUtil.containsAll(emptyArray, values);
    }

    private static String objToString(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        if (TypeUtils.isSimple(value)) {
            return value.toString();
        }
        return JsonUtils.toJson(value);
    }

    public static boolean isObjectJson(String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        return ReUtil.isMatch("^\\{[\\w\\W]*\\}$", value);
    }

    public static boolean isArrayJson(String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        return ReUtil.isMatch("^\\[[\\w\\W]*\\]$", value);
    }
}
