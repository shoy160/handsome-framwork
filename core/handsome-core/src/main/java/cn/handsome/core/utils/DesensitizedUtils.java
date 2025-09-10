package cn.handsome.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author luoyong
 * @date 2024/5/16
 */
@Slf4j
public class DesensitizedUtils {
    public static void desensitizedPhone(Object data, String... keys) {
        Map<String, DesensitizedUtil.DesensitizedType> keyTypes = Stream.of(keys)
                .collect(HashMap::new, (k, v) -> k.put(v, DesensitizedUtil.DesensitizedType.MOBILE_PHONE), HashMap::putAll);
        desensitized(data, keyTypes);
    }

    public static void desensitizedByTypes(Object data, Map<DesensitizedUtil.DesensitizedType, String[]> typeMap) {
        Map<String, DesensitizedUtil.DesensitizedType> keys = new HashMap<>();
        typeMap.forEach((k, v) -> {
            for (String key : v) {
                keys.putIfAbsent(key, k);
            }
        });
        desensitized(data, keys);
    }

    public static void desensitized(Object data, Map<String, DesensitizedUtil.DesensitizedType> keys) {
        if (Objects.isNull(data) || TypeUtils.isSimple(data) || MapUtil.isEmpty(keys)) {
            return;
        }
        if (data instanceof Iterable) {
            ((Iterable<?>) data).forEach(t -> desensitized(t, keys));
            return;
        }
        Map<String, Object> map = MapUtils.map(data);
        map.forEach((k, v) -> {
            if (v instanceof Iterable) {
                List<Object> values = new ArrayList<>();
                ((Iterable<?>) v).forEach(t -> {
                    if (t instanceof Map) {
                        desensitized(t, keys);
                    }
                    if (t instanceof Iterable) {
                        ((Iterable<?>) t).forEach(d -> desensitized(d, keys));
                    }
                    if (keys.containsKey(k) && TypeUtils.isString(t)) {
                        values.add(desensitizedValue(k, t, keys));
                    }
                });
                if (CollUtil.isNotEmpty(values)) {
                    map.put(k, values);
                }
            }
            if (v instanceof Map) {
                desensitized(v, keys);
            }
            if (TypeUtils.isString(v) && keys.containsKey(k)) {
                map.put(k, desensitizedValue(k, v, keys));
            }
        });
    }

    public static Object desensitizedValue(String key, Object value, Map<String, DesensitizedUtil.DesensitizedType> keys) {
        if (Objects.isNull(value) || StrUtil.isBlank(key) || MapUtil.isEmpty(keys)) {
            return value;
        }
        DesensitizedUtil.DesensitizedType desensitizedType = keys.get(key);
        if (Objects.isNull(desensitizedType)) {
            return value;
        }
        return DesensitizedUtil.desensitized(String.valueOf(value), desensitizedType);
    }
}
