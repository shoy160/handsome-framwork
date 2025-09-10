package cn.handsome.scim.utils;

import cn.handsome.scim.model.ScimFilter;
import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimUser;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public class FilterEvaluator {
    public static List<ScimUser> filterUsers(List<ScimUser> users, ScimFilter filter) {
        List<ScimUser> result = new ArrayList<>();
        for (ScimUser user : users) {
            if (evaluate(user, filter)) {
                result.add(clone(user, ScimUser.class));
            }
        }
        return result;
    }

    public static List<ScimGroup> filterGroups(List<ScimGroup> groups, ScimFilter filter) {
        List<ScimGroup> result = new ArrayList<>();
        for (ScimGroup group : groups) {
            if (evaluate(group, filter)) {
                result.add(clone(group, ScimGroup.class));
            }
        }
        return result;
    }

    private static <T> T clone(T obj, Class<T> clazz) {
        return BeanUtil.toBeanIgnoreError(obj, clazz);
    }

    private static boolean evaluate(Object entity, ScimFilter filter) {
        if (filter == null) {
            return true;
        }

        switch (filter.getOperator()) {
            case "and":
                return evaluate(entity, filter.getLeft()) && evaluate(entity, filter.getRight());
            case "or":
                return evaluate(entity, filter.getLeft()) || evaluate(entity, filter.getRight());
            case "eq":
                return evaluateEquals(entity, filter.getAttribute(), filter.getValue());
            case "ne":
                return !evaluateEquals(entity, filter.getAttribute(), filter.getValue());
            case "co":
                return evaluateContains(entity, filter.getAttribute(), filter.getValue());
            case "sw":
                return evaluateStartsWith(entity, filter.getAttribute(), filter.getValue());
            case "ew":
                return evaluateEndsWith(entity, filter.getAttribute(), filter.getValue());
            case "gt":
                return evaluateGreaterThan(entity, filter.getAttribute(), filter.getValue());
            case "lt":
                return evaluateLessThan(entity, filter.getAttribute(), filter.getValue());
            case "ge":
                return evaluateGreaterThanOrEqual(entity, filter.getAttribute(), filter.getValue());
            case "le":
                return evaluateLessThanOrEqual(entity, filter.getAttribute(), filter.getValue());
            default:
                throw new IllegalArgumentException("Unsupported operator: " + filter.getOperator());
        }
    }

    private static boolean evaluateEquals(Object entity, String attribute, String value) {
        Object attributeValue = getAttributeValue(entity, attribute);
        if (attributeValue == null) {
            return false;
        }
        return attributeValue.toString().equals(value);
    }

    private static boolean evaluateContains(Object entity, String attribute, String value) {
        Object attributeValue = getAttributeValue(entity, attribute);
        if (attributeValue == null) {
            return false;
        }
        return attributeValue.toString().contains(value);
    }

    private static boolean evaluateStartsWith(Object entity, String attribute, String value) {
        Object attributeValue = getAttributeValue(entity, attribute);
        if (attributeValue == null) {
            return false;
        }
        return attributeValue.toString().startsWith(value);
    }

    private static boolean evaluateEndsWith(Object entity, String attribute, String value) {
        Object attributeValue = getAttributeValue(entity, attribute);
        if (attributeValue == null) {
            return false;
        }
        return attributeValue.toString().endsWith(value);
    }

    private static boolean evaluateGreaterThan(Object entity, String attribute, String value) {
        return compare(entity, attribute, value) > 0;
    }

    private static boolean evaluateLessThan(Object entity, String attribute, String value) {
        return compare(entity, attribute, value) < 0;
    }

    private static boolean evaluateGreaterThanOrEqual(Object entity, String attribute, String value) {
        return compare(entity, attribute, value) >= 0;
    }

    private static boolean evaluateLessThanOrEqual(Object entity, String attribute, String value) {
        return compare(entity, attribute, value) <= 0;
    }

    private static int compare(Object entity, String attribute, String value) {
        Object attributeValue = getAttributeValue(entity, attribute);
        if (attributeValue == null) {
            return -1;
        }

        try {
            // 尝试数字比较
            Number num1 = (Number) attributeValue;
            Number num2 = Double.valueOf(value);
            return Double.compare(num1.doubleValue(), num2.doubleValue());
        } catch (ClassCastException | NumberFormatException e) {
            // 字符串比较
            return attributeValue.toString().compareTo(value);
        }
    }

    @SuppressWarnings("unchecked")
    private static Object getAttributeValue(Object entity, String attributePath) {
        if (!attributePath.contains(".")) {
            return getSimpleAttributeValue(entity, attributePath);
        }

        String[] pathSegments = attributePath.split("\\.");
        Object current = entity;

        for (String segment : pathSegments) {
            if (current == null) {
                return null;
            }

            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(segment);
            } else {
                current = getSimpleAttributeValue(current, segment);
            }
        }

        return current;
    }

    private static Object getSimpleAttributeValue(Object entity, String attribute) {
        try {
            Field field = getField(entity.getClass(), attribute);
            if (field == null) {
                return null;
            }

            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(superClass, fieldName);
            }
            return null;
        }
    }
}
