package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class GreaterCompare extends BaseCompare {
    public GreaterCompare() {
        super(ConditionOp.GREATER);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return compareValue(value, compareTo) > 0;
    }
}
