package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/6/21
 */
@NoArgsConstructor
@Data
public class MsgSetReadReq {
    @JsonProperty("Report_Account")
    private String reportAccount;
    @JsonProperty("Peer_Account")
    private String peerAccount;
}
