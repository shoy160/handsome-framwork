package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.enums.MsgTypeEnum;
import cn.handsome.sdk.im.model.message.BaseMessage;
import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
public class GroupMsgHistoryResp extends RestResp {
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("IsFinished")
    private Integer isFinished;
    @JsonProperty("RspMsgList")
    private List<RspMsgListDTO> rspMsgList;

    @NoArgsConstructor
    @Data
    public static class RspMsgListDTO {
        @JsonProperty("From_Account")
        private String fromAccount;
        @JsonProperty("IsPlaceMsg")
        private Integer isPlaceMsg;
        @JsonProperty("MsgBody")
        private List<MsgBodyDTO> msgBody;
        @JsonProperty("MsgPriority")
        private Integer msgPriority;
        @JsonProperty("MsgRandom")
        private Integer msgRandom;
        @JsonProperty("MsgSeq")
        private Integer msgSeq;
        @JsonProperty("MsgTimeStamp")
        private Integer msgTimeStamp;

        @NoArgsConstructor
        @Data
        public static class MsgBodyDTO {
            @JsonProperty("MsgContent")
            private BaseMessage msgContent;
            @JsonProperty("MsgType")
            private MsgTypeEnum msgType;
        }
    }
}
