package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import cn.handsome.core.lang.Func;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Json辅助类
 *
 * @author shay
 * @date 2020/8/15
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper;

    public static void setMapper(ObjectMapper mapper) {
        JsonUtils.mapper = mapper;
    }

    private static synchronized ObjectMapper getMapper() {
        if (null != JsonUtils.mapper) {
            return JsonUtils.mapper;
        }
        ObjectMapper mapper = new ObjectMapper();
        //忽略未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //忽略大小写
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JsonUtils.mapper = mapper;
        return mapper;
    }

    public static String toJson(Object source) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.writeValueAsString(source);
        } catch (Exception ex) {
            log.warn("json序列化异常", ex);
            return Constants.STR_EMPTY;
        }
    }

    public static <T> T json(String content, Class<T> clazz) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.readValue(content, clazz);
        } catch (Exception ex) {
            log.warn("json反序列化异常", ex);
            return null;
        }
    }


    public static <T> T json(String content, Func<JavaType, TypeFactory> func) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.readValue(content, func.invoke(mapper.getTypeFactory()));
        } catch (Exception ex) {
            log.warn("json反序列化异常", ex);
            return null;
        }
    }

    public static <T> T json(String content, TypeReference<T> typeReference) {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.readValue(content, typeReference);
        } catch (Exception ex) {
            log.warn("json反序列化异常", ex);
            return null;
        }
    }

    public static <TK, TV> Map<TK, TV> jsonMap(String content, Class<TK> keyClass, Class<TV> valueClass) {
        return json(content, f -> f.constructMapType(Map.class, keyClass, valueClass));
    }

    public static Map<String, Object> jsonMap(String content) {
        return jsonMap(content, String.class, Object.class);
    }


    public static JsonNode node(String content) {
        return node(content, null);
    }


    public static JsonNode node(String content, String nodeName) {
        try {
            ObjectMapper mapper = getMapper();
            JsonNode node = mapper.readTree(content);
            if (CommonUtils.isNotEmpty(nodeName)) {
                node = node.findValue(nodeName);
            }
            return node;
        } catch (Exception ex) {
            log.warn("json反序列化异常", ex);
            return null;
        }
    }


    public static <T> T json(String content, String nodeName, Class<T> clazz) {
        JsonNode value = node(content, nodeName);
        if (value == null) {
            return null;
        }
        return json(value.toString(), clazz);
    }

    public static <T> List<T> jsonList(String content, Class<T> clazz) {
        try {
            ObjectMapper mapper = getMapper();
            JavaType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return mapper.readValue(content, listType);
        } catch (Exception ex) {
            log.warn("json反序列化异常", ex);
            return null;
        }
    }

    public static <T> List<T> jsonList(String content, String nodeName, Class<T> clazz) {
        JsonNode value = node(content, nodeName);
        if (value == null) {
            return new ArrayList<>();
        }
        return jsonList(value.toString(), clazz);
    }
}
