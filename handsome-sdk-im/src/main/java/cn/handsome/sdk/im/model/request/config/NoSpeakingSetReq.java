package cn.handsome.sdk.im.model.request.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/6/21
 */
@NoArgsConstructor
@Data
public class NoSpeakingSetReq {
    @JsonProperty("Set_Account")
    private String setAccount;
    @JsonProperty("C2CmsgNospeakingTime")
    private Long c2cMsgNoSpeakingTime;
    @JsonProperty("GroupmsgNospeakingTime")
    private Integer groupMsgNoSpeakingTime;
}
