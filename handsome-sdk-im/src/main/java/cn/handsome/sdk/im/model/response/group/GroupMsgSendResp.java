package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/22
 */
@Getter
@Setter
public class GroupMsgSendResp extends RestResp {
    @JsonProperty("MsgTime")
    private Integer msgTime;
    @JsonProperty("MsgSeq")
    private Integer msgSeq;
}
