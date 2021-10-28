package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 撤回消息
 *
 * @author shoy
 * @date 2021/6/21
 */
@NoArgsConstructor
@Data
public class MsgRecallReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private String toAccount;
    @JsonProperty("MsgKey")
    private String msgKey;
}
