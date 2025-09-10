package cn.handsome.core.compare.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Getter
@RequiredArgsConstructor
public enum ConditionConjunction {
    /**
     * AND
     */
    and("and"),
    /**
     * OR
     */
    or("or");
    private final String value;
}
