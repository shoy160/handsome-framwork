package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.handsome.core.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class InArrayCompare extends BaseCompare {
    public InArrayCompare() {
        super(ConditionOp.IN_ARRAY);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        List<Object> realList = new ArrayList<>();
        if (compareTo instanceof String) {
            realList = JsonUtils.jsonList(compareTo.toString(), Object.class);
        } else if (compareTo instanceof Iterable) {
            ((Iterable<?>) compareTo).forEach(realList::add);
        }
        return realList.stream().anyMatch(item -> Objects.equals(item, value));
    }
}
