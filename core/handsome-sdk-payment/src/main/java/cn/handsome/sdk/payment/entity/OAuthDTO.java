package cn.handsome.sdk.payment.entity;

import cn.handsome.core.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shoy
 * @date 2021/10/9
 */
@Getter
@Setter
@ToString
public class OAuthDTO extends BaseDTO {
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String openId;
    private String unionId;
    private String scope;
    private String errCode;
    private String errMsg;
}
