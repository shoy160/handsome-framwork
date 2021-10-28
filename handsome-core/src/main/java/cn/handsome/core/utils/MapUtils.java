package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import cn.handsome.core.lang.Func;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * todo
 *
 * @author shay
 * @date 2020/8/15
 */
public class MapUtils {

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
        return toUrl(map, Constants.EMPTY_STR, true);
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
                value = Constants.EMPTY_STR;
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
}
