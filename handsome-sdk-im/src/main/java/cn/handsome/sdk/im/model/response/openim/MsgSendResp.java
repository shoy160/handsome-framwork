package cn.handsome.sdk.im.model.response.openim;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class MsgSendResp extends RestResp {
    @JsonProperty("MsgTime")
    private Integer msgTime;
    @JsonProperty("MsgKey")
    private String msgKey;
}
