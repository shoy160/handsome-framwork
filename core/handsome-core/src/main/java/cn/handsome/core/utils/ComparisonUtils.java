package cn.handsome.core.utils;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 对比类辅助
 *
 * @author luoyong
 * @date 2023/6/15
 */
public final class ComparisonUtils {
    /**
     * Map 字段是否变更
     * 只要有一个属性发生变更及算已变更
     *
     * @param source     原数据
     * @param target     目标数据
     * @param ignoreNull 是否忽略目标数据中的 null 值
     * @return 是否变更
     */
    public static boolean isFieldChanged(Map<String, Object> source, Map<String, Object> target, boolean ignoreNull) {
        if (MapUtil.isEmpty(target)) {
            return false;
        }
        if (MapUtil.isEmpty(source)) {
            return true;
        }
        for (Map.Entry<String, Object> entry : target.entrySet()) {
            Object targetValue = entry.getValue();
            if (ignoreNull && Objects.isNull(targetValue)) {
                continue;
            }
            Object sourceValue = source.get(entry.getKey());
            String sourceStr = JsonUtils.toJson(sourceValue);
            String targetStr = JsonUtils.toJson(targetValue);
            if (isFieldChanged(sourceStr, targetStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字段值是否变更
     * null,"","{}" 相等; null,"","[]" 相等; "{}" "[]" 不等
     * json 字符处理，Map、List 不区分顺序
     *
     * @param source 原字段值
     * @param target 目标字段值
     * @return 是否变更
     */
    public static boolean isFieldChanged(String source, String target) {
        if (isAllEmpty(source, target)) {
            return false;
        }
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
                    || sourceList.stream()
                    .anyMatch(s -> targetList.stream().noneMatch(t -> ObjectUtil.equal(s, t)));
        }
        return ObjectUtil.notEqual(source, target);
    }

    /**
     * Map 是否相等
     *
     * @param source 原 Map
     * @param target 目标 Map
     * @return
     */
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

    /**
     * 是否全为空，包括 json 字符 "{}" "[]"
     *
     * @param values 字符值列表
     * @return 是否全为空
     */
    public static boolean isAllEmpty(CharSequence... values) {
        if (ArrayUtil.isEmpty(values)) {
            return true;
        }
        final CharSequence[] emptyObject = {null, "", "{}"};
        final CharSequence[] emptyArray = {null, "", "[]"};
        return ArrayUtil.containsAll(emptyObject, values) || ArrayUtil.containsAll(emptyArray, values);
    }

    /**
     * 是否 object json 字符 简易判断
     *
     * @param value 字符值
     * @return 是否 object json
     */
    public static boolean isObjectJson(String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        return ReUtil.isMatch("^\\{[\\w\\W]*\\}$", value);
    }

    /**
     * 是否 array json 字符 简易判断
     *
     * @param value 字符值
     * @return 是否 array json
     */
    public static boolean isArrayJson(String value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        return ReUtil.isMatch("^\\[[\\w\\W]*\\]$", value);
    }
}
