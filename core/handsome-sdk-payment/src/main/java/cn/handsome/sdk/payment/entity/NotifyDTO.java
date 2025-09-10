package cn.handsome.sdk.payment.entity;

import cn.handsome.sdk.payment.enums.PaymentMode;
import cn.handsome.sdk.payment.enums.PaymentType;
import cn.handsome.sdk.payment.enums.TradeStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 异步回调实体
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@ToString
public class NotifyDTO implements Serializable {
    private String orderNo;
    private Long amount;
    private String extend;
    private PaymentMode mode;
    private PaymentType type;
    private TradeStatus status;
    private String tradeNo;
    private String sign;
}
