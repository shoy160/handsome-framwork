package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.enums.MsgFlagEnum;
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
public class GroupMemberResp {
    /**
     * 成员
     */
    @JsonProperty("Member_Account")
    private String memberAccount;
    /**
     * 群内角色
     * Owner 群主、Admin 群管理员以及 Member 群成员
     */
    @JsonProperty("Role")
    private GroupRoleEnum role;

    /**
     * 入群时间（UTC 时间）
     */
    @JsonProperty("JoinTime")
    private Long joinTime;

    /**
     * 成员名片
     */
    @JsonProperty("NameCard")
    private String nameCard;

    /**
     * 该成员当前已读消息 Seq
     */
    @JsonProperty("MsgSeq")
    private Integer msgSeq;

    /**
     * 消息接收选项
     */
    @JsonProperty("MsgFlag")
    private MsgFlagEnum msgFlag;

    /**
     * 最后发言时间（UTC 时间）
     */
    @JsonProperty("LastSendMsgTime")
    private Long lastSendMsgTime;

    /**
     * 禁言截止时间（UTC 时间）
     */
    @JsonProperty("ShutUpUntil")
    private Long shutUpUntil;

    /**
     * 群成员维度自定义字段（选填）
     */
    @JsonProperty("AppMemberDefinedData")
    private List<KeyValue> appMemberDefinedData;
}
