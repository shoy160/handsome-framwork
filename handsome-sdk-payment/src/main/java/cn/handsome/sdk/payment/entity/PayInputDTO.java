package cn.handsome.sdk.payment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 创建支付入参
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@ToString
public class PayInputDTO extends BasePayDTO {
    private String openId;
    private SceneInfoDTO sceneInfo;

    public PayInputDTO() {
    }

    public PayInputDTO(long amount, String orderNo) {
        this.setAmount(amount);
        this.setOrderNo(orderNo);
    }
}
