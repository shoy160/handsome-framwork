package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import cn.handsome.core.lang.Func;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Json辅助类
 *
 * @author shay
 * @date 2020/8/15
 */
@Slf4j
public class JsonUtils {
    private static final String REG_JSON = "^(\\{[\\w\\W]*\\})|(\\[[\\w\\W]*\\])$";

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


    public enum JsonType {
        /**
         * 对象
         */
        OBJECT,
        /**
         * 数组
         */
        ARRAY,
        /**
         * 基础类型
         */
        SCALAR,
        /**
         * 非 json
         */
        NOT_JSON
    }

    /**
     * 判断字符串是否为 JSON 格式，并区分对象和数组
     */
    public static JsonType getJsonType(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return JsonType.NOT_JSON;
        }

        jsonStr = jsonStr.trim();
        if (!ReUtil.isMatch(REG_JSON, jsonStr)) {
            return JsonType.NOT_JSON;
        }
        try {
            ObjectMapper mapper = getMapper();
            // 解析 JSON 字符串
            JsonNode jsonNode = mapper.readTree(jsonStr);
            if (jsonNode.isObject()) {
                return JsonType.OBJECT;
            } else if (jsonNode.isArray()) {
                return JsonType.ARRAY;
            } else {
                // 可能是基本类型（如数字、字符串）的 JSON
                return JsonType.SCALAR;
            }
        } catch (Exception e) {
            // 解析失败，不是 JSON
            return JsonType.NOT_JSON;
        }
    }

    public static boolean isObjectJsonStr(String value) {
        return JsonType.OBJECT.equals(getJsonType(value));
    }

    public static boolean isArrayJsonStr(String value) {
        return JsonType.ARRAY.equals(getJsonType(value));
    }

    public static boolean isJsonStr(String value) {
        return !JsonType.NOT_JSON.equals(getJsonType(value));
    }

    public static String toJson(Object source) {
        if (Objects.isNull(source)) {
            return null;
        }
        try {
            ObjectMapper mapper = getMapper();
            return mapper.writeValueAsString(source);
        } catch (Exception ex) {
            log.warn("json序列化异常", ex);
            return Constants.STR_EMPTY;
        }
    }

    public static String toPrettyJson(Object source) {
        if (Objects.isNull(source)) {
            return null;
        }
        try {
            ObjectMapper mapper = getMapper().copy();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(source);
        } catch (Exception ex) {
            log.warn("json序列化异常", ex);
            return Constants.STR_EMPTY;
        }
    }

    public static <T> T json(String content, Class<T> clazz) {
        if (StrUtil.isBlank(content)) {
            return null;
        }
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
            return new ArrayList<>();
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
