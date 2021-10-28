package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.SwitchEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupModifyReq {
    /**
     * 自定义群组 ID (必填)
     */
    @JsonProperty("GroupId")
    private String groupId;

    /**
     * 群名称 (选填)
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
     * 设置全员禁言（选填）
     */
    @JsonProperty("ShutUpAllMember")
    private SwitchEnum shutUpAllMember;

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
}
