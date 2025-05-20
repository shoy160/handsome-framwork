package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.ReUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class RegexCompare extends BaseCompare {
    public RegexCompare() {
        super(ConditionOp.REGEX);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        if (Objects.isNull(value) || Objects.isNull(compareTo)) {
            return false;
        }
        return ReUtil.isMatch(compareTo.toString(), value.toString());
    }
}
