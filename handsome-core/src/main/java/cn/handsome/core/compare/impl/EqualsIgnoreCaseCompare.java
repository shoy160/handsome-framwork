package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class EqualsIgnoreCaseCompare extends BaseCompare {
    public EqualsIgnoreCaseCompare() {
        super(ConditionOp.EQ_IGNORE_CASE);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        if (Objects.isNull(value)) {
            return Objects.isNull(compareTo);
        }
        if (Objects.isNull(compareTo)) {
            return false;
        }
        return StrUtil.equalsIgnoreCase(String.valueOf(value), String.valueOf(compareTo));
    }
}
