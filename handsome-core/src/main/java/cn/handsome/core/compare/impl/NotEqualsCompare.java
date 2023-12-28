package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.ObjUtil;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class NotEqualsCompare extends BaseCompare {
    public NotEqualsCompare() {
        super(ConditionOp.NOT_EQ);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return ObjUtil.notEqual(value, compareTo);
    }
}
