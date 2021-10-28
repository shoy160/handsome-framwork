package cn.handsome.sdk.payment.enums;

import cn.handsome.core.enums.BaseNamedEnum;
import lombok.AllArgsConstructor;

/**
 * 交易状态
 *
 * @author shay
 * @date 2020/8/7
 */
@AllArgsConstructor
public enum TradeStatus implements BaseNamedEnum {
    /**
     * 待支付
     */
    Wait(0, "待支付"),
    Paid(1, "已支付"),
    Close(2, "已关闭"),
    Refund(3, "已退款");
    private final Integer value;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
