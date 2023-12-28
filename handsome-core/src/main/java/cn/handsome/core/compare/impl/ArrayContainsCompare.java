package cn.handsome.core.compare.impl;


import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class ArrayContainsCompare extends BaseCompare {
    public ArrayContainsCompare() {
        super(ConditionOp.ARRAY_CONTAINS);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        List<Object> arrayList = new ArrayList<>();
        if (value instanceof Iterable) {
            ((Iterable<?>) value).forEach(arrayList::add);
        } else {
            arrayList.add(value);
        }
        return arrayList.contains(compareTo);
    }
}
