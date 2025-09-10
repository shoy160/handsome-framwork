package cn.handsome.sdk.payment.enums;

import cn.handsome.core.enums.BaseNamedEnum;
import lombok.AllArgsConstructor;

/**
 * 支付方式
 *
 * @author shay
 * @date 2020/8/7
 */
@AllArgsConstructor
public enum PaymentMode implements BaseNamedEnum {

    /**
     * 支付宝
     */
    Alipay(0, "支付宝"),
    /**
     * 微信支付
     */
    WeChat(1, "微信支付");
    private final int value;
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
