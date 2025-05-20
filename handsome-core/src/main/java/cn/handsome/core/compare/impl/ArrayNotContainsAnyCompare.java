package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import org.springframework.stereotype.Component;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class ArrayNotContainsAnyCompare extends BaseCompare {
    public ArrayNotContainsAnyCompare() {
        super(ConditionOp.ARRAY_NOT_CONTAINS_ANY);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return !contains(value, compareTo, true);
    }
}
