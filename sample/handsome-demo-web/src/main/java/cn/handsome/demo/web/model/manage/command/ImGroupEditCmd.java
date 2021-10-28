package cn.handsome.demo.web.model.manage.command;

import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.SwitchEnum;
import cn.handsome.web.model.command.BaseCmd;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author shoy
 * @date 2021/8/26
 */
@Getter
@Setter
@ToString
public class ImGroupEditCmd extends BaseCmd {
    /**
     * 群名称 (选填)
     */
    private String name;

    /**
     * 群简介 (选填)
     */
    private String introduction;

    /**
     * 群公告 (选填)
     */
    private String notification;

    /**
     * 群头像 (选填)
     */
    private String faceUrl;

    /**
     * 最大群成员数量 (选填)
     * 私有群是200，公开群是2000，聊天室是6000，音视频聊天室和在线成员广播大群无限制
     */
    private Integer maxMemberCount;

    /**
     * 设置全员禁言（选填）
     */
    private SwitchEnum shutUpAllMember;

    /**
     * 申请加群处理方式 (选填)
     * 默认为 NeedPermission（需要验证）
     */
    private ApplyJoinOptionEnum applyJoinOption;

    /**
     * 群组维度的自定义字段 (选填)
     */
    private List<KeyValue> appDefinedData;
}
