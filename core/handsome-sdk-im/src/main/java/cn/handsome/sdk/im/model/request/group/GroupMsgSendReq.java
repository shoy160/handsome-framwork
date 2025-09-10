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
public class GroupMsgSendReq {
    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 指定消息发送者（选填）
     */
    @JsonProperty("From_Account")
    private String fromAccount;

    @JsonProperty("MsgPriority")
    private String msgPriority;

    /**
     * 指定消息不触发更新最近联系人会话
     */
    @JsonProperty("SendMsgControl")
    private List<String> sendMsgControl;

    @JsonProperty("ForbidCallbackControl")
    private List<String> forbidCallbackControl;

    /**
     * 随机数字
     * 五分钟数字相同认为是重复消息
     */
    @JsonProperty("Random")
    private Integer random;
    @JsonProperty("MsgBody")
    private List<MsgBodyDTO> msgBody;
    @JsonProperty("OfflinePushInfo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OfflinePushInfoDTO offlinePushInfo;

    @JsonProperty("GroupAtInfo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GroupAtInfoDTO> groupAtInfo;
    /**
     * 1表示消息仅发送在线成员，默认0表示发送所有成员
     * AVChatRoom(直播群)不支持该参数
     */
    @JsonProperty("OnlineOnlyFlag")
    private Integer onlineOnlyFlag;


    @NoArgsConstructor
    @Data
    public static class OfflinePushInfoDTO {
        @JsonProperty("PushFlag")
        private Integer pushFlag;
        @JsonProperty("Desc")
        private String desc;
        @JsonProperty("Ext")
        private String ext;
        @JsonProperty("AndroidInfo")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private AndroidInfoDTO androidInfo;
        @JsonProperty("ApnsInfo")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ApnsInfoDTO apnsInfo;

        @NoArgsConstructor
        @Data
        public static class AndroidInfoDTO {
            @JsonProperty("Sound")
            private String sound;
        }

        @NoArgsConstructor
        @Data
        public static class ApnsInfoDTO {
            @JsonProperty("Sound")
            private String sound;
            @JsonProperty("BadgeMode")
            private Integer badgeMode;
            @JsonProperty("Title")
            private String title;
            @JsonProperty("SubTitle")
            private String subTitle;
            @JsonProperty("Image")
            private String image;
        }
    }

    @NoArgsConstructor
    @Data
    public static class MsgBodyDTO {
        @JsonProperty("MsgType")
        private String msgType;
        @JsonProperty("MsgContent")
        private Object msgContent;
    }

    @NoArgsConstructor
    @Data
    public static class GroupAtInfoDTO {
        @JsonProperty("GroupAtAllFlag")
        private Integer groupAtAllFlag;
        @JsonProperty("GroupAt_Account")
        private String groupatAccount;
    }
}
