package cn.handsome.core.utils;

import cn.handsome.core.exception.BusinessException;
import cn.hutool.core.util.ObjectUtil;

/**
 * 校验工具类
 *
 * @author shay
 * @date 2020/11/11
 */
public final class AssertUtils {

    /**
     * 断言 - Null
     *
     * @param value   value
     * @param message message
     */
    public static void isNull(Object value, String message) {
        if (value == null) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - 非Null
     *
     * @param value   value
     * @param message message
     */
    public static void isNotNull(Object value, String message) {
        if (value != null) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - 空
     *
     * @param value   value
     * @param message message
     */
    public static void isEmpty(Object value, String message) {
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - 非空
     *
     * @param value   value
     * @param message message
     */
    public static void isNotEmpty(Object value, String message) {
        if (ObjectUtil.isNotEmpty(value)) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - True
     *
     * @param value   value
     * @param message message
     */
    public static void isTrue(Boolean value, String message) {
        if (value != null && value) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - True
     *
     * @param value   value
     * @param message message
     */
    public static void isTrue(boolean value, String message) {
        if (value) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - False
     *
     * @param value   value
     * @param message message
     */
    public static void isFalse(Boolean value, String message) {
        if (value == null || !value) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - False
     *
     * @param value   value
     * @param message message
     */
    public static void isFalse(boolean value, String message) {
        if (!value) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - 相等
     *
     * @param value   value
     * @param source  source
     * @param message message
     */
    public static void isEquals(Object value, Object source, String message) {
        if ((value == null)) {
            if (source == null) {
                return;
            }
        } else if (value.equals(source)) {
            return;
        }
        throw new BusinessException(message);
    }

    /**
     * 断言 - 不相等
     *
     * @param value   value
     * @param source  source
     * @param message message
     */
    public static void isNotEquals(Object value, Object source, String message) {
        if ((value == null)) {
            if (source != null) {
                return;
            }
        } else if (!value.equals(source)) {
            return;
        }
        throw new BusinessException(message);
    }
}
