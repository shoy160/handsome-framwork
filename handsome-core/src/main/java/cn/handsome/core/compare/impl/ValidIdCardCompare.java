package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.util.IdcardUtil;
import org.springframework.stereotype.Component;

/**
 * @author luoyong
 * @date 2023/6/25
 */
@Component
public class ValidIdCardCompare extends BaseCompare {
    public ValidIdCardCompare() {
        super(ConditionOp.IS_VALID_ID_CARD_NUMBER);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        return IdcardUtil.isValidCard(String.valueOf(value));
    }
}
