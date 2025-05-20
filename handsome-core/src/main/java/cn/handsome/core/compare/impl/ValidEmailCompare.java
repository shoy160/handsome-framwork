package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.ReUtil;
import org.springframework.stereotype.Component;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class ValidEmailCompare extends BaseCompare {
    public ValidEmailCompare() {
        super(ConditionOp.IS_VALID_EMAIL);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return ReUtil.isMatch("\\w+@(\\w+.)+[a-z]{2,3}", String.valueOf(value));
    }
}
