package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;

import java.util.List;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class OnlyInCertainValuesCompare extends BaseCompare {
    public OnlyInCertainValuesCompare() {
        super(ConditionOp.ONLY_IN_CERTAIN_VALUES);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        List<String> validItemsStr = toStringList(compareTo);
        return validItemsStr.contains(String.valueOf(value));
    }
}
