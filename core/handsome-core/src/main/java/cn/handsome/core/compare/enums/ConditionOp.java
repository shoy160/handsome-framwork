package cn.handsome.core.compare.enums;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public enum ConditionOp {
    // 为空
    EMPTY,
    // 不为空
    NOT_EMPTY,
    // 等于
    EQ,
    // 等于（忽略大小写）
    EQ_IGNORE_CASE,
    // 不等于
    NOT_EQ,
    // 大于
    GREATER,
    // 小于
    LESSER,
    // 在什么什么之间
    // BETWEEN,
    /**
     * 字符串包含
     */
    STRING_CONTAINS,
    /**
     * 字符串不包含
     */
    STRING_NOT_CONTAINS,
    /**
     * 数组包含
     */
    ARRAY_CONTAINS,
    /**
     * 数组包含任意
     */
    ARRAY_CONTAINS_ANY,
    /**
     * 数组不包含
     */
    ARRAY_NOT_CONTAINS,
    /**
     * 数组不包含任意
     */
    ARRAY_NOT_CONTAINS_ANY,
    /**
     * 在某个数组中
     */
    IN_ARRAY,
    /**
     * 不在某个数组中
     */
    NOT_IN_ARRAY,
    /**
     * 字符串以某个字符结尾
     */
    STRING_ENDS_WITH,
    /**
     * 字符串以某个字符开始
     */
    STRING_STARTS_WITH,
    IS_VALID_PHONE,
    IS_VALID_EMAIL,
    IS_VALID_ID_CARD_NUMBER,
    EXPRESSION,
    REGEX,
    ONLY_IN_CERTAIN_VALUES,
    /**
     * 另一个数组的子集
     */
    SUBLIST_OF_ANOTHER_ARRAY,
    /**
     * 不是另一个数组的子集
     */
    NOT_SUB_OF_ANOTHER_ARRAY,
    /**
     * 最近
     */
    RECENT,
    RECENT_DATE,
    RECENT_TIME
}
