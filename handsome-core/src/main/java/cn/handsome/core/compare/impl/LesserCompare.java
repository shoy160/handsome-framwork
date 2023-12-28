package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class LesserCompare extends BaseCompare {
    public LesserCompare() {
        super(ConditionOp.LESSER);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return compareValue(value, compareTo) < 0;
    }
}
