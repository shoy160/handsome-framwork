package cn.handsome.core.compare.enums;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public enum ConditionOp {
    /**
     * 为空
     */
    EMPTY,
    /**
     * 不为空
     */
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
    // 字符串包含
    STRING_CONTAINS,
    // 数组包含
    ARRAY_CONTAINS,
    // 在某个数组中
    IN_ARRAY,
    STRING_ENDSWITH,
    STRING_STARTSWITH,
    IS_VALID_PHONE,
    IS_VALID_EMAIL,
    IS_VALID_ID_CARD_NUMBER,
    EXPRESSION,
    REGEX,
    ONLY_IN_CERTAIN_VALUES,
    SUBLIST_OF_ANOTHER_ARRAY,
    /**
     * 最近
     */
    RECENT,
    RECENT_DATE,
    RECENT_TIME
}
