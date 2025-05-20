package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import org.springframework.stereotype.Component;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class ArrayNotContainsCompare extends BaseCompare {
    public ArrayNotContainsCompare() {
        super(ConditionOp.ARRAY_NOT_CONTAINS);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return !contains(value, compareTo, false);
    }
}
