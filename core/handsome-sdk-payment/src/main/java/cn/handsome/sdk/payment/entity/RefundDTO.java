package cn.handsome.sdk.payment.entity;

import java.io.Serializable;

/**
 * @author shay
 * @date 2020/10/29
 */
public class RefundDTO implements Serializable {
    private String tradeNo;
    private String refundNo;
    private Long refundAmount;
}
