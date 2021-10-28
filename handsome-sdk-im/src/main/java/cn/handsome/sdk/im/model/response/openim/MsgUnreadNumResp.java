package cn.handsome.sdk.im.model.response.openim;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgUnreadNumResp extends RestResp {

    @JsonProperty("C2CUnreadMsgNumList")
    private List<C2cUnreadMsgNumListDTO> c2cUnreadMsgNumList;

    @JsonProperty("AllC2CUnreadMsgNum")
    private Integer allC2cUnreadMsgNum;

    @NoArgsConstructor
    @Data
    public static class C2cUnreadMsgNumListDTO {
        @JsonProperty("Peer_Account")
        private String peerAccount;
        @JsonProperty("C2CUnreadMsgNum")
        private Integer c2cUnreadMsgNum;
    }
}
