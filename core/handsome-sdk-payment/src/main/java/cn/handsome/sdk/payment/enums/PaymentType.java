package cn.handsome.sdk.payment.enums;

import cn.handsome.core.enums.BaseNamedEnum;
import lombok.AllArgsConstructor;

/**
 * Todo
 *
 * @author shay
 * @date 2020/8/7
 */
@AllArgsConstructor
public enum PaymentType implements BaseNamedEnum {
    /**
     * 网页支付
     */
    Web(0, "网页支付"),
    H5(1, "H5支付"),
    App(2, "App支付"),
    Public(3, "公众号支付"),
    Scan(4, "扫码支付"),
    Barcode(5, "条码支付"),
    Applet(6, "小程序支付");
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
