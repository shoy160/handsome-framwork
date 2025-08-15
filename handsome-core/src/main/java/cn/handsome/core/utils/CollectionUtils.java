package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import cn.handsome.core.lang.Func;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author luoyong
 * @date 2022/12/12
 */
public final class CollectionUtils {
    public static <T, V> Collection<V> distinct(Collection<T> sourceList, Func<V, T> valueFunc) {
        if (null == sourceList || null == valueFunc) {
            return new ArrayList<>(0);
        }
        return sourceList
                .stream()
                .map(valueFunc::invoke)
                .filter(ObjectUtil::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> convertToMapList(Object data) {
        return convertToList(data, MapUtils::map, Constants.STR_EMPTY);
    }

    public static <T> List<T> convertToList(Object data, Class<T> clazz) {
        return convertToList(data, clazz, Constants.STR_EMPTY);
    }

    public static <T> List<T> convertToList(Object data, Class<T> clazz, String split) {
        return convertToList(data, t -> convert(t, clazz), split);
    }

    public static <T> List<T> convertToList(Object data, Class<T> clazz, boolean convertJson, String split) {
        return convertToList(data, t -> convert(t, clazz), convertJson, split);
    }

    public static <T> List<T> convertToList(
            Object data, Function<Object, T> convertFunc, String split
    ) {
        return convertToList(data, convertFunc, true, split);
    }

    public static <T> List<T> convertToList(
            Object data, Function<Object, T> convertFunc, boolean convertJson, String split
    ) {
        List<T> results = new ArrayList<>();
        if (Objects.isNull(data)) {
            return results;
        }
        try {
            if (data instanceof Iterable) {
                for (Object item : (Iterable<?>) data) {
                    results.add(convertFunc.apply(item));
                }
                return results;
            }
            if (TypeUtils.isString(data) && convertJson) {
                String strValue = data.toString();
                if (JsonUtils.isArrayJsonStr(strValue)) {
                    return JsonUtils.jsonList(strValue, Object.class)
                            .stream().map(convertFunc)
                            .collect(Collectors.toList());
                }
                if (StrUtil.isNotBlank(split)) {
                    return Arrays.stream(strValue.split(split))
                            .map(convertFunc)
                            .collect(Collectors.toList());
                }
            }
            results.add(convertFunc.apply(data));
            return results;
        } catch (Exception ex) {
            throw new RuntimeException(String.format("节点参数配置异常:%s", ExceptionUtil.getRootCauseMessage(ex)));
        }
    }

    public static List<Object> convertToList(Object data) {
        return convertToList(data, Constants.STR_EMPTY);
    }

    public static List<Object> convertToList(Object data, String split) {
        return convertToList(data, Object.class, split);
    }

    public static List<String> convertToStringList(Object value, boolean convertJson, String split) {
        return convertToList(value, String.class, convertJson, split);
    }

    public static List<String> convertToStringList(Object value, String split) {
        return convertToList(value, String.class, split);
    }

    public static List<String> convertToStringList(Object value) {
        return convertToList(value, String.class);
    }

    public static List<Object> convertToObjectList(Object value) {
        return convertToList(value);
    }

    public static <T> T convert(Object value, Class<T> clazz) {
        if (Objects.equals(value.getClass(), clazz) || Object.class.equals(clazz)) {
            return clazz.cast(value);
        }
        if (TypeUtils.isSimple(clazz)) {
            return Convert.convert(clazz, value);
        }
        if (TypeUtils.isString(value)) {
            return JsonUtils.json(value.toString(), clazz);
        }
        return BeanUtil.toBean(value, clazz);
    }

    public static boolean contains(Object array, Object value) {
        if (Objects.isNull(value)) {
            return false;
        }
        List<Object> list = convertToObjectList(array);
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        List<Object> values = convertToObjectList(value);
        if (CollUtil.isEmpty(values)) {
            return false;
        }
        if (values.size() > 1) {
            return new HashSet<>(list).containsAll(values);
        }
        value = convert(values.get(0), list.iterator().next().getClass());
        return list.contains(value);
    }
}
