package cn.handsome.sdk.im.model.response.config;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/6/21
 */
@NoArgsConstructor
@Data
public class NoSpeakingGetResp extends RestResp {
    @JsonProperty("C2CmsgNospeakingTime")
    private Long c2cMsgNoSpeakingTime;
    @JsonProperty("GroupmsgNospeakingTime")
    private Integer groupMsgNoSpeakingTime;
}
