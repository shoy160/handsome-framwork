package cn.handsome.sdk.im.model.response.openim;

import cn.handsome.sdk.im.model.enums.MsgTypeEnum;
import cn.handsome.sdk.im.model.response.RestResp;
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
public class MsgGetResp extends RestResp {
    @JsonProperty("Complete")
    private Integer complete;
    @JsonProperty("MsgCnt")
    private Integer msgCnt;
    @JsonProperty("LastMsgTime")
    private Integer lastMsgTime;
    @JsonProperty("LastMsgKey")
    private String lastMsgKey;
    @JsonProperty("MsgList")
    private List<MsgListDTO> msgList;

    @NoArgsConstructor
    @Data
    public static class MsgListDTO {
        @JsonProperty("From_Account")
        private String fromAccount;
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("MsgSeq")
        private Integer msgSeq;
        @JsonProperty("MsgRandom")
        private Integer msgRandom;
        @JsonProperty("MsgTimeStamp")
        private Integer msgTimeStamp;
        @JsonProperty("MsgFlagBits")
        private Integer msgFlagBits;
        @JsonProperty("MsgKey")
        private String msgKey;
        @JsonProperty("MsgBody")
        private List<MsgBodyDTO> msgBody;
        @JsonProperty("CloudCustomData")
        private String cloudCustomData;

        @NoArgsConstructor
        @Data
        public static class MsgBodyDTO {
            @JsonProperty("MsgType")
            private MsgTypeEnum msgType;
            @JsonProperty("MsgContent")
            private Object msgContent;
        }
    }
}
