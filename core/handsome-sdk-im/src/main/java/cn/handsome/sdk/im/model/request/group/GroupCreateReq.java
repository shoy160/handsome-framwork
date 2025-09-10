package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 创建群组请求实体
 *
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupCreateReq {
    /**
     * 群主 ID (选填)
     */
    @JsonProperty("Owner_Account")
    private String ownerAccount;

    /**
     * 群组形态 (必填)
     */
    @JsonProperty("Type")
    private GroupTypeEnum type;

    /**
     * 自定义群组 ID (选填)
     */
    @JsonProperty("GroupId")
    private String groupId;

    /**
     * 群名称 (必填)
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 群简介 (选填)
     */
    @JsonProperty("Introduction")
    private String introduction;

    /**
     * 群公告 (选填)
     */
    @JsonProperty("Notification")
    private String notification;

    /**
     * 群头像 (选填)
     */
    @JsonProperty("FaceUrl")
    private String faceUrl;

    /**
     * 最大群成员数量 (选填)
     * 私有群是200，公开群是2000，聊天室是6000，音视频聊天室和在线成员广播大群无限制
     */
    @JsonProperty("MaxMemberCount")
    private Integer maxMemberCount;

    /**
     * 申请加群处理方式 (选填)
     * 默认为 NeedPermission（需要验证）
     */
    @JsonProperty("ApplyJoinOption")
    private ApplyJoinOptionEnum applyJoinOption;

    /**
     * 群组维度的自定义字段 (选填)
     */
    @JsonProperty("AppDefinedData")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KeyValue> appDefinedData;

    /**
     * 初始群成员列表，最多500个 (选填)
     */
    @JsonProperty("MemberList")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GroupMemberReq> memberList;
}
