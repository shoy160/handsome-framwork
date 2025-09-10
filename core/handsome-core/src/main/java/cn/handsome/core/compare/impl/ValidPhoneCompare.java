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
public class ValidPhoneCompare extends BaseCompare {
    public ValidPhoneCompare() {
        super(ConditionOp.IS_VALID_PHONE);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return ReUtil.isMatch("^1[3,5]\\d{9}||18[6,8,9]\\d{8}$", String.valueOf(value));
    }
}
