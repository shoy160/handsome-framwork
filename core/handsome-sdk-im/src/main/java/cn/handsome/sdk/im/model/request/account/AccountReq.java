package cn.handsome.sdk.im.model.request.account;

import cn.handsome.sdk.im.model.BaseIdentifier;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class AccountReq extends BaseIdentifier {
    @JsonProperty("Nick")
    private String nick;
    @JsonProperty("FaceUrl")
    private String faceUrl;
}
