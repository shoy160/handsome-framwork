package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMsgImportReq {
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("MsgList")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MsgListDTO> msgList;

    @NoArgsConstructor
    @Data
    public static class MsgListDTO {
        @JsonProperty("From_Account")
        private String fromAccount;
        @JsonProperty("SendTime")
        private Integer sendTime;
        @JsonProperty("Random")
        private Integer random;
        @JsonProperty("MsgBody")
        private List<MsgBodyDTO> msgBody;

        @NoArgsConstructor
        @Data
        public static class MsgBodyDTO {
            @JsonProperty("MsgType")
            private String msgType;
            @JsonProperty("MsgContent")
            private Object msgContent;
        }
    }
}
