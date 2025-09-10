package cn.handsome.sdk.payment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 收银台入参
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@ToString
public class CashierInputDTO extends BasePayDTO {
    /**
     * 是否多码合一
     */
    private Boolean scan;
}
