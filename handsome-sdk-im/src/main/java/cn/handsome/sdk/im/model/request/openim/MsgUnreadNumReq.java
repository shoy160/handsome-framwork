package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/21
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgUnreadNumReq {
    @JsonProperty("To_Account")
    private String toAccount;
    @JsonProperty("Peer_Account")
    private List<String> peerAccount;
}
