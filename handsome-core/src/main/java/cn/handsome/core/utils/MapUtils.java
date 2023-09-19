package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import cn.handsome.core.lang.Func;
import cn.handsome.core.lang.Tuple;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo
 *
 * @author shay
 * @date 2020/8/15
 */
public class MapUtils {
    private static final Pattern ARRAY_PATTERN = PatternPool.get("\\[(\\d+)\\]$", Pattern.DOTALL);

    public static Map<String, Object> map(Object obj) {
        return map(obj, null);
    }

    public static Map<String, Object> map(Object obj, Func<String, String> keyEditor) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        try {
            if (obj instanceof Map) {
                Map<?, ?> item = (Map<?, ?>) obj;
                for (Object key : item.keySet()) {
                    String mapKey = key.toString();
                    if (null != keyEditor) {
                        mapKey = keyEditor.invoke(mapKey);
                    }
                    if (StrUtil.isBlank(mapKey)) {
                        continue;
                    }
                    map.put(mapKey, item.get(key));
                }
                return map;
            }
            return BeanUtil.beanToMap(obj, new LinkedHashMap<>(), false, t -> {
                if (null != keyEditor) {
                    return keyEditor.invoke(t);
                }
                return t;
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            return map;
        }
    }

    public static String toUrl(Map<String, Object> map) {
        return toUrl(map, Constants.STR_EMPTY, true);
    }

    public static String toUrl(Map<String, Object> map, String charset) {
        return toUrl(map, charset, true);
    }


    public static String toUrl(Map<String, Object> map, String charset, boolean filterEmpty) {
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            if (filterEmpty && CommonUtils.isEmpty(key)) {
                continue;
            }
            String value = CommonUtils.unEscape(map.get(key));
            if (value == null) {
                if (filterEmpty) {
                    continue;
                }
                value = Constants.STR_EMPTY;
            } else {
                if (CommonUtils.isNotEmpty(charset)) {
                    try {
                        value = URLEncoder.encode(value, charset);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            builder.append(String.format("%s=%s&", key, value));
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }
        return builder.toString();
    }

    public static <T> T getValueByPath(Map<String, Object> map, Class<T> clazz, String paths) {
        return getValue(map, clazz, paths.split("\\."));
    }

    public static <T> T getValue(Map<String, Object> map, Class<T> clazz, String... paths) {
        Object currentValue = getValue(map, paths);
        return Objects.isNull(currentValue) ? null : Convert.convert(clazz, currentValue);
    }

    public static Object getValue(Map<String, Object> map, String... paths) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        Pattern pattern = PatternPool.get("\\[(\\d+)\\]$", Pattern.DOTALL);
        Object currentValue = map;
        for (String path : paths) {
            if (!(currentValue instanceof Map)) {
                return null;
            }
            Map<String, Object> currentMap = Convert.toMap(String.class, Object.class, currentValue);
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                //下标处理
                int index = Convert.toInt(matcher.group(1));
                String key = path.replace(matcher.group(0), Constants.STR_EMPTY);
                Object value = currentMap.get(key);
                if (!TypeUtils.isArray(value)) {
                    return null;
                }
                ArrayList<?> arrayList = (ArrayList<?>) value;
                if (index >= arrayList.size()) {
                    return null;
                }
                currentValue = arrayList.get(index);
                continue;
            }
            currentValue = currentMap.get(path);
        }
        return currentValue;
    }

    public static boolean setValue(Map<String, Object> map, String paths, Object value) {
        return setValue(map, value, paths.split("\\."));
    }

    public static boolean setValue(Map<String, Object> map, Object value, String... paths) {
        return setValue(map, value, true, paths);
    }

    public static boolean setValue(
            Map<String, Object> map, Object value, boolean initArray, String... paths
    ) {
        if (Objects.isNull(map)) {
            return false;
        }
        Pattern pattern = PatternPool.get("\\[(\\d+)\\]$", Pattern.DOTALL);
        Object currentValue = map;
        final int pathLength = paths.length;
        for (int i = 0; i < pathLength; i++) {
            String path = paths[i];
            if (!(currentValue instanceof Map)) {
                return false;
            }
            boolean isLatestPath = Objects.equals(i, pathLength - 1);
            Map<String, Object> currentMap = (Map<String, Object>) currentValue;
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                //下标处理
                int index = Convert.toInt(matcher.group(1));
                String key = path.replace(matcher.group(0), Constants.STR_EMPTY);
                Object arrayValue = currentMap.get(key);
                if (Objects.isNull(arrayValue) && initArray) {
                    arrayValue = new ArrayList<>();
                    currentMap.put(key, arrayValue);
                }
                if (!TypeUtils.isArray(arrayValue)) {
                    return false;
                }
                ArrayList<Object> arrayList = (ArrayList<Object>) arrayValue;
                if (isLatestPath) {
                    if (index <= arrayList.size() - 1) {
                        arrayList.set(index, value);
                    } else {
                        arrayList.add(value);
                    }
                    return true;
                } else {
                    if (index <= arrayList.size() - 1) {
                        currentValue = arrayList.get(index);
                    } else {
                        currentValue = new HashMap<>(0);
                        arrayList.add(currentValue);
                    }
                }
                continue;
            }
            if (isLatestPath) {
                currentMap.put(path, value);
                return true;
            }
            currentValue = currentMap.get(path);
            if (Objects.isNull(currentValue)) {
                currentValue = new HashMap<>(0);
                currentMap.put(path, currentValue);
            }
        }
        return false;
    }

    public static <T> T tryGetValue(Class<T> clazz, Map<String, Object> data, String... keys) {
        if (MapUtil.isEmpty(data)) {
            return null;
        }
        for (String key : keys) {
            T value = MapUtil.get(data, key, clazz);
            if (ObjectUtil.isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    public static String tryGetStrValue(Map<String, Object> data, String... keys) {
        return tryGetValue(String.class, data, keys);
    }

    public static Object popValue(Map<String, Object> data, String... keys) {
        if (MapUtil.isEmpty(data)) {
            return null;
        }
        for (String key : keys) {
            Object value = data.remove(key);
            if (Objects.nonNull(value)) {
                return value;
            }
        }
        return null;
    }

    private static Tuple<String, Integer> getPathKey(String path) {
        Matcher matcher = ARRAY_PATTERN.matcher(path);
        if (matcher.find()) {
            //下标处理
            int index = Convert.toInt(matcher.group(1));
            String key = path.replace(matcher.group(0), Constants.STR_EMPTY);
            return Tuple.of(key, index);
        }
        return Tuple.of(path, null);
    }
}
