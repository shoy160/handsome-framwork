package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.ObjUtil;

import java.util.Objects;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class EmptyCompare extends BaseCompare {
    public EmptyCompare() {
        super(ConditionOp.EMPTY);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return Objects.isNull(value) || ObjUtil.isEmpty(value);
    }
}
