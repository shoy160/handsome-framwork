package cn.handsome.sdk.payment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Todo
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@ToString
public abstract class BasePayDTO implements Serializable {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 支付金额(分)
     */
    private Long amount;
    /**
     * 支付标题
     */
    private String title;
    /**
     * 支付内容
     */
    private String body;
    /**
     * 扩展字段
     */
    private String extend;
    /**
     * 超时时间(秒)
     */
    private Integer timeout;
    /**
     * 同步回调地址
     */
    private String redirectUrl;
}
