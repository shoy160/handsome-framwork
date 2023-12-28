package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.StrUtil;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class StringEndsWithCompare extends BaseCompare {
    public StringEndsWithCompare() {
        super(ConditionOp.STRING_ENDSWITH);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return StrUtil.endWith(String.valueOf(value), String.valueOf(compareTo));
    }
}
