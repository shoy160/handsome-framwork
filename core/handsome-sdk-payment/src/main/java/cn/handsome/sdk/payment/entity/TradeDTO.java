package cn.handsome.sdk.payment.entity;

import cn.handsome.core.domain.dto.BaseDTO;
import cn.handsome.sdk.payment.enums.PaymentMode;
import cn.handsome.sdk.payment.enums.TradeStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/8/11
 */
@Getter
@Setter
@ToString
public class TradeDTO extends BaseDTO {
    private String id;
    /**
     * 支付方式
     */
    private PaymentMode mode;
    /**
     * 交易状态
     */
    private TradeStatus status;
    /**
     * 支付类型
     */
    private String type;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单金额
     */
    private Long amount;
    /**
     * 支付标题
     */
    private String title;
    /**
     * 支付描述
     */
    private String body;
    /**
     * 同步调整链接
     */
    private String redirectUrl;
    /**
     * 平台ID
     */
    private String platformId;
    /**
     * 支付用户
     */
    private String paidUser;
    /**
     * 支付账号
     */
    private String paidAccount;
    /**
     * 第三方支付交易号
     */
    private String outTradeNo;
    /**
     * 支付时间
     */
    private Date paidTime;
}
