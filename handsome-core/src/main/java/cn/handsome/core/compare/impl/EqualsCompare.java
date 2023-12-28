package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.ObjUtil;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class EqualsCompare extends BaseCompare {
    public EqualsCompare() {
        super(ConditionOp.EQ);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return ObjUtil.equal(value, compareTo);
    }
}
