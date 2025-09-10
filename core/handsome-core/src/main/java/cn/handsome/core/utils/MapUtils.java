package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo
 *
 * @author shay
 * @date 2020/8/15
 */
@Slf4j
public class MapUtils {
    private static final String REG_PATH_SPLIT = "\\.";
    private static final String REG_PATH_ARRAY = "\\[(\\d+)\\]$";
    private static final Pattern PATH_PATTERN = PatternPool.get(REG_PATH_ARRAY, Pattern.DOTALL);

    public static Map<String, Object> map(Object obj) {
        return map(obj, null);
    }

    public static Map<String, Object> map(Object obj, Function<String, String> keyEditor) {
        Map<String, Object> map = new HashMap<>();
        if (Objects.isNull(obj)) {
            return map;
        }
        try {
            if (TypeUtils.isString(obj)) {
                // json
                map = JsonUtils.jsonMap(obj.toString());
                return mapKeyEditor(map, keyEditor);
            }
            if (obj instanceof Map) {
                // Map
                try {
                    map = (Map<String, Object>) obj;
                    return mapKeyEditor(map, keyEditor);
                } catch (Exception ignored) {
                }
                map = new HashMap<>();
                Map<?, ?> item = (Map<?, ?>) obj;
                for (Object key : item.keySet()) {
                    String mapKey = key.toString();
                    if (null != keyEditor) {
                        mapKey = keyEditor.apply(mapKey);
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
                    return keyEditor.apply(t);
                }
                return t;
            });
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
            return map;
        }
    }

    private static Map<String, Object> mapKeyEditor(
            Map<String, Object> map, Function<String, String> keyEditor
    ) {
        if (MapUtil.isEmpty(map)) {
            return new HashMap<>(0);
        }
        if (Objects.isNull(keyEditor)) {
            return map;
        }
        Map<String, Object> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = keyEditor.apply(entry.getKey());
            if (StrUtil.isBlank(key)) {
                continue;
            }
            newMap.put(key, entry.getValue());
        }
        return newMap;
    }

    public static Map<String, Object> filter(Map<String, Object> data, Collection<String> keys) {
        if (MapUtil.isEmpty(data) || CollUtil.isEmpty(keys)) {
            return new HashMap<>(0);
        }
        return data.entrySet().stream()
                .filter(t -> keys.contains(t.getKey()))
                .collect(HashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), HashMap::putAll);
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
                        log.warn("URL encode error", ex);
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

    public static <T> T getValue(
            Map<String, Object> map, String keyOrPath, Class<T> clazz, T defaultValue
    ) {
        T value = getValue(map, keyOrPath, clazz);
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static <T> T getValue(Map<String, Object> map, String keyOrPath, Class<T> clazz) {
        if (StrUtil.isBlank(keyOrPath)) {
            return null;
        }
        Object currentValue = get(map, keyOrPath);
        return Objects.isNull(currentValue) ? null : Convert.convert(clazz, currentValue);
    }

    public static String getStr(Map<String, Object> map, String keyOrPath) {
        return getValue(map, keyOrPath, String.class);
    }

    public static String getStr(Map<String, Object> map, String keyOrPath, String defValue) {
        return getValue(map, keyOrPath, String.class, defValue);
    }

    public static Integer getInt(Map<String, Object> map, String keyOrPath) {
        return getValue(map, keyOrPath, Integer.class);
    }

    public static Long getLong(Map<String, Object> map, String keyOrPath) {
        return getValue(map, keyOrPath, Long.class);
    }

    public static boolean getBool(Map<String, Object> map, String keyOrPath) {
        Boolean value = getValue(map, keyOrPath, Boolean.class);
        return Objects.nonNull(value) && value;
    }

    public static boolean getBool(Map<String, Object> map, String keyOrPath, boolean defValue) {
        return getValue(map, keyOrPath, Boolean.class, defValue);
    }

    public static boolean contains(Map<String, Object> map, String... paths) {
        if (MapUtil.isEmpty(map) || ArrayUtil.isEmpty(paths)
                || Arrays.stream(paths).allMatch(StrUtil::isBlank)) {
            return false;
        }
        paths = Arrays.stream(paths)
                .filter(StrUtil::isNotBlank)
                .flatMap(t -> Arrays.stream(t.split(REG_PATH_SPLIT)))
                .filter(StrUtil::isNotBlank)
                .toArray(String[]::new);
        Pattern pattern = PatternPool.get(REG_PATH_ARRAY, Pattern.DOTALL);
        Object currentValue = map;
        for (String path : paths) {
            Map<String, Object> currentMap = map(currentValue);
            if (MapUtil.isEmpty(currentMap)) {
                return false;
            }
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                //下标处理
                int index = Convert.toInt(matcher.group(1));
                String key = path.replace(matcher.group(0), Constants.STR_EMPTY);
                Object value = currentMap.get(key);
                if (!TypeUtils.isArray(value)) {
                    return false;
                }
                if (value instanceof Iterable) {
                    int i = 0;
                    boolean match = false;
                    for (Object item : (Iterable<?>) value) {
                        if (Objects.equals(i++, index)) {
                            currentValue = item;
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        continue;
                    }
                    return false;
                }
            }
            if (currentMap.containsKey(path)) {
                currentValue = currentMap.get(path);
            } else {
                return false;
            }
        }
        return true;
    }

    public static Object get(Map<String, Object> map, String... paths) {
        if (MapUtil.isEmpty(map) || ArrayUtil.isEmpty(paths)
                || Arrays.stream(paths).allMatch(StrUtil::isBlank)) {
            return null;
        }
        paths = Arrays.stream(paths)
                .filter(StrUtil::isNotBlank)
                .flatMap(t -> Arrays.stream(t.split(REG_PATH_SPLIT)))
                .filter(StrUtil::isNotBlank)
                .toArray(String[]::new);
        Pattern pattern = PatternPool.get(REG_PATH_ARRAY, Pattern.DOTALL);
        Object currentValue = map;
        for (String path : paths) {
            Map<String, Object> currentMap = map(currentValue);
            if (MapUtil.isEmpty(currentMap)) {
                return null;
            }
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                //下标处理
                int index = Convert.toInt(matcher.group(1));
                String key = path.replace(matcher.group(0), Constants.STR_EMPTY);
                Object value = currentMap.get(key);
                if (!TypeUtils.isArray(value)) {
                    return null;
                }
                if (value instanceof Iterable) {
                    int i = 0;
                    boolean match = false;
                    for (Object item : (Iterable<?>) value) {
                        if (Objects.equals(i++, index)) {
                            currentValue = item;
                            match = true;
                            break;
                        }
                    }
                    if (match) {
                        continue;
                    }
                    return null;
                }
            }
            currentValue = currentMap.get(path);
        }
        return currentValue;
    }

    public static boolean set(Map<String, Object> map, String keyOrPath, Object value) {
        return set(map, value, keyOrPath.split("\\."));
    }

    public static boolean set(
            Map<String, Object> map, Object value, String... paths
    ) {
        return set(map, value, true, paths);
    }

    public static boolean set(
            Map<String, Object> map, Object value, boolean initArray, String... paths
    ) {
        if (Objects.isNull(map)) {
            return false;
        }
        Object currentValue = map;
        final int pathLength = paths.length;
        for (int i = 0; i < pathLength; i++) {
            String path = paths[i];
            if (!(currentValue instanceof Map)) {
                return false;
            }
            boolean isLatestPath = Objects.equals(i, pathLength - 1);
            Map<String, Object> currentMap = MapUtils.map(currentValue);
            Matcher matcher = PATH_PATTERN.matcher(path);
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
                List<Object> arrayList = new ArrayList<>();
                if (arrayValue instanceof Iterable) {
                    ((Iterable<?>) arrayValue).forEach(arrayList::add);
                    currentMap.put(key, arrayList);
                }
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
            T value = MapUtils.getValue(data, key, clazz);
            if (ObjUtil.isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 尝试获取 key 值，兼容不同命名风格
     *
     * @param data data
     * @param key  key
     * @return value
     */
    public static Object tryGet(Map<String, Object> data, String key) {
        if (StrUtil.isBlank(key) || MapUtil.isEmpty(data)) {
            return null;
        }
        Object value = get(data, key);
        if (Objects.nonNull(value)) {
            return value;
        }
        String underLineKey = StrUtil.toUnderlineCase(key);
        if (!key.equals(underLineKey) && Objects.nonNull(value = get(data, underLineKey))) {
            return value;
        }
        String camelCaseKey = StrUtil.toCamelCase(key);
        if (!key.equals(camelCaseKey) && Objects.nonNull(value = get(data, camelCaseKey))) {
            return value;
        }
        return null;
    }

    public static String tryGetStr(Map<String, Object> data, String... keys) {
        return tryGetValue(String.class, data, keys);
    }

    public static Object pop(Map<String, Object> data, String... keys) {
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

    public static <T> T popValue(Map<String, Object> data, Class<T> clazz, String... keys) {
        Object value = pop(data, keys);
        if (Objects.isNull(value)) {
            return null;
        }
        if (TypeUtils.isSimple(clazz)) {
            return Convert.convert(clazz, value);
        }
        return BeanUtil.toBeanIgnoreError(value, clazz);
    }

    public static String popStrWithDef(Map<String, Object> data, String defValue, String... keys) {
        String value = popValue(data, String.class, keys);
        return StrUtil.isBlank(value) ? defValue : value;
    }

    public static String popStr(Map<String, Object> data, String... keys) {
        return popValue(data, String.class, keys);
    }

    /**
     * 反转 Map
     * value -> key, key -> value
     */
    public static <K, V> Map<V, K> inversion(Map<K, V> map) {
        if (MapUtil.isEmpty(map)) {
            return new HashMap<>(0);
        }
        return map.entrySet().stream()
                .collect(HashMap::new, (k, v) -> k.put(v.getValue(), v.getKey()), HashMap::putAll);
    }

    public static Map<String, Object> getMapPure(Object map) {
        Map<String, Object> config = new HashMap<>(map(map));
        config.remove("resource");
        config.remove("operation");
        return config;
    }

    public static <T, V> Map<T, V> combine(Map<T, V> map, Map<T, V> otherMap, boolean override) {
        Map<T, V> result = Objects.isNull(map) ? new HashMap<>(0) : new HashMap<>(map);
        if (override) {
            result.putAll(otherMap);
        } else {
            otherMap.forEach(result::putIfAbsent);
        }
        return result;
    }

    public static <T, V> Map<T, V> combine(Map<T, V> map, Map<T, V> otherMap) {
        return combine(map, otherMap, true);
    }

    @SafeVarargs
    public static <T, V> Map<T, V> combine(Map<T, V> map, boolean override, Map<T, V>... otherMaps) {
        Map<T, V> result = Objects.isNull(map) ? new HashMap<>(0) : new HashMap<>(map);
        for (Map<T, V> otherMap : otherMaps) {
            combine(result, otherMap, override);
        }
        return result;
    }
}
