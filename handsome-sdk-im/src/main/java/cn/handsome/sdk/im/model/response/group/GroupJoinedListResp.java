package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupJoinedListResp extends RestResp {

    @JsonProperty("TotalCount")
    private Integer totalCount;
    @JsonProperty("GroupIdList")
    private List<GroupIdListDTO> groupIdList;

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GroupIdListDTO {
        @JsonProperty("ApplyJoinOption")
        private String applyJoinOption;
        @JsonProperty("CreateTime")
        private Integer createTime;
        @JsonProperty("FaceUrl")
        private String faceUrl;
        @JsonProperty("GroupId")
        private String groupId;
        @JsonProperty("Introduction")
        private String introduction;
        @JsonProperty("LastInfoTime")
        private Integer lastInfoTime;
        @JsonProperty("LastMsgTime")
        private Integer lastMsgTime;
        @JsonProperty("MaxMemberNum")
        private Integer maxMemberNum;
        @JsonProperty("MemberNum")
        private Integer memberNum;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("NextMsgSeq")
        private Integer nextMsgSeq;
        @JsonProperty("Notification")
        private String notification;
        @JsonProperty("Owner_Account")
        private String ownerAccount;
        @JsonProperty("SelfInfo")
        private SelfInfoDTO selfInfo;
        @JsonProperty("ShutUpAllMember")
        private String shutUpAllMember;
        @JsonProperty("Type")
        private String type;

        @Data
        @NoArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class SelfInfoDTO {
            @JsonProperty("JoinTime")
            private Integer joinTime;
            @JsonProperty("MsgFlag")
            private String msgFlag;
            @JsonProperty("Role")
            private String role;
            @JsonProperty("MsgSeq")
            private Integer msgSeq;
        }
    }
}
