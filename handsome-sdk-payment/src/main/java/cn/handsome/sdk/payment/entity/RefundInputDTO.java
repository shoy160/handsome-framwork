package cn.handsome.sdk.payment.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author shay
 * @date 2020/10/29
 */
@Getter
@Setter
public class RefundInputDTO implements Serializable {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退款金额(分),为空全退
     */
    private Long amount;
    /**
     * 退款订单号
     */
    private String refundNo;
    /**
     * 退款描述
     */
    private String reason;
}
