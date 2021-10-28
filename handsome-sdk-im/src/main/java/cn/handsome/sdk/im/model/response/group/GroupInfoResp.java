package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import cn.handsome.sdk.im.model.enums.SwitchEnum;
import cn.handsome.sdk.im.model.request.group.GroupMemberReq;
import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class GroupInfoResp extends RestResp {
    @JsonProperty("GroupInfo")
    private List<GroupInfo> groupInfo;

    @Getter
    @Setter
    public static class GroupInfo {
        @JsonProperty("ErrorInfo")
        private String errorInfo;

        @JsonProperty("ErrorCode")
        private Integer errorCode;

        /**
         * 群组形态
         */
        @JsonProperty("Type")
        private GroupTypeEnum type;

        /**
         * 自定义群组 ID
         */
        @JsonProperty("GroupId")
        private String groupId;

        /**
         * 群名称 (必填)
         */
        @JsonProperty("Name")
        private String name;

        /**
         * 群主 ID
         */
        @JsonProperty("Owner_Account")
        private String ownerAccount;

        /**
         * 群简介
         */
        @JsonProperty("Introduction")
        private String introduction;

        /**
         * 群公告
         */
        @JsonProperty("Notification")
        private String notification;
        /**
         * 群头像
         */
        @JsonProperty("FaceUrl")
        private String faceUrl;

        /**
         * 群组创建时间（UTC 时间）
         */
        @JsonProperty("CreateTime")
        private Long createTime;

        /**
         * 最后群资料变更时间（UTC 时间）
         */
        @JsonProperty("LastInfoTime")
        private Long lastInfoTime;

        /**
         * 群内最后一条消息的时间（UTC 时间）
         */
        @JsonProperty("LastMsgTime")
        private Long lastMsgTime;

        /**
         * 群内下一条消息的 Seq
         */
        @JsonProperty("NextMsgSeq")
        private Integer nextMsgSeq;

        /**
         * 当前成员数量
         */
        @JsonProperty("MemberNum")
        private Integer memberNum;

        /**
         * 最大群成员数量
         * 私有群是200，公开群是2000，聊天室是6000，音视频聊天室和在线成员广播大群无限制
         */
        @JsonProperty("MaxMemberCount")
        private Integer maxMemberCount;

        /**
         * 申请加群处理方式
         * 默认为 NeedPermission（需要验证）
         */
        @JsonProperty("ApplyJoinOption")
        private ApplyJoinOptionEnum applyJoinOption;

        /**
         * 设置全员禁言
         */
        @JsonProperty("ShutUpAllMember")
        private SwitchEnum shutUpAllMember;

        /**
         * 群组维度的自定义字段
         */
        @JsonProperty("AppDefinedData")
        private List<KeyValue> appDefinedData;

        /**
         * 群成员列表
         */
        @JsonProperty("MemberList")
        private List<GroupMemberReq> memberList;
    }
}
