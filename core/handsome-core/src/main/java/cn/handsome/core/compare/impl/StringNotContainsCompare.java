package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class StringNotContainsCompare extends BaseCompare {
    public StringNotContainsCompare() {
        super(ConditionOp.STRING_NOT_CONTAINS);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return !StrUtil.contains(String.valueOf(value), String.valueOf(compareTo));
    }
}
