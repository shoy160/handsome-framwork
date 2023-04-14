package cn.handsome.sdk.im;

import cn.handsome.core.Constants;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import cn.handsome.sdk.im.model.enums.MsgTypeEnum;
import cn.handsome.sdk.im.model.enums.CustomGroupTypeEnum;
import cn.handsome.sdk.im.model.message.BaseMessage;
import cn.handsome.sdk.im.model.message.CustomMessage;
import cn.handsome.sdk.im.model.message.TextMessage;
import cn.handsome.sdk.im.model.request.group.GroupCreateReq;
import cn.handsome.sdk.im.model.request.group.GroupModifyReq;
import cn.handsome.sdk.im.model.request.openim.MsgSendReq;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.group.GroupCreateResp;
import cn.handsome.sdk.im.model.response.group.GroupInfoList;
import cn.handsome.sdk.im.model.response.group.GroupMemberAddResp;
import cn.handsome.sdk.im.model.response.openim.MsgSendResp;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 简化版IM接口
 *
 * @author shoy
 * @date 2021/8/6
 */
public interface ImSimplifyClient {

    ImClient client();

    /**
     * 创建账号
     *
     * @param id       id
     * @param nickname 昵称
     * @param faceUrl  头像
     * @param profiles 用户资料
     * @return resp
     */
    RestResp createAccount(String id, String nickname, String faceUrl, Map<String, Object> profiles);

    /**
     * 创建账号
     *
     * @param id       id
     * @param nickname 昵称
     * @param faceUrl  头像
     * @return resp
     */
    default RestResp createAccount(String id, String nickname, String faceUrl) {
        return createAccount(id, nickname, faceUrl, null);
    }

    /**
     * 创建账号
     *
     * @param id       id
     * @param nickname 昵称
     * @param faceUrl  头像
     * @param role     用户角色
     * @return resp
     */
    default RestResp createAccountByRole(String id, String nickname, String faceUrl, Integer role) {
        Map<String, Object> profiles = new HashMap<>(1);
        profiles.put(ImConstants.TAG_PROFILE_ROLE, role);
        return createAccount(id, nickname, faceUrl, profiles);
    }

    /**
     * 创建群组
     *
     * @param createReq req
     * @param groupType groupType
     * @return resp
     */
    GroupCreateResp createGroup(GroupCreateReq createReq, CustomGroupTypeEnum groupType);

    /**
     * 创建群组
     *
     * @param createReq req
     * @return resp
     */
    default GroupCreateResp createGroup(GroupCreateReq createReq) {
        return createGroup(createReq, null);
    }

    /**
     * 修改群组
     *
     * @param modifyReq req
     * @return resp
     */
    RestResp modifyGroup(GroupModifyReq modifyReq);

    /**
     * 群组列表
     *
     * @param type 类型
     * @param next next
     * @param size size
     * @return list
     */
    GroupInfoList groups(GroupTypeEnum type, int size, long next);

    /**
     * 群组列表
     *
     * @param type 类型
     * @param size size
     * @return list
     */
    default GroupInfoList groups(GroupTypeEnum type, int size) {
        return groups(type, size, 0L);
    }

    /**
     * 转让群主
     *
     * @param groupId 群组ID
     * @param ownerId 群主ID
     * @return resp
     */
    RestResp transferGroupOwner(String groupId, String ownerId);

    /**
     * 添加群组成员
     *
     * @param groupId 群组ID
     * @param silence 是否静默加入
     * @param userIds 用户IDs
     * @return resp
     */
    GroupMemberAddResp addGroupMember(String groupId, boolean silence, String... userIds);

    /**
     * 添加群组成员
     *
     * @param groupId 群组ID
     * @param userIds 用户IDs
     * @return resp
     */
    default GroupMemberAddResp addGroupMember(String groupId, String... userIds) {
        return addGroupMember(groupId, true, userIds);
    }

    /**
     * 删除群组成员
     *
     * @param groupId 群组ID
     * @param silence 是否静默删除
     * @param reason  删除原因
     * @param userIds 用户IDs
     * @return resp
     */
    RestResp deleteGroupMember(String groupId, boolean silence, String reason, String... userIds);

    /**
     * 静默删除群组成员
     *
     * @param groupId 群组ID
     * @param reason  删除原因
     * @param userIds 用户IDs
     * @return resp
     */
    default RestResp deleteGroupMember(String groupId, String reason, String... userIds) {
        return deleteGroupMember(groupId, true, reason, userIds);
    }

    /**
     * 群组成员角色
     *
     * @param groupId 群组ID
     * @param userId  成员ID
     * @return role
     */
    GroupRoleEnum groupMemberRole(String groupId, String userId);

    /**
     * 是否群组成员
     *
     * @param groupId 群组ID
     * @param userId  成员ID
     * @return boolean
     */
    default boolean isGroupMember(String groupId, String userId) {
        GroupRoleEnum role = groupMemberRole(groupId, userId);
        return role != GroupRoleEnum.NotMember;
    }

    /**
     * 解散群组
     *
     * @param groupId groupId
     * @return resp
     */
    RestResp destroyGroup(String groupId);

    /**
     * 发送文本消息
     *
     * @param fromId  from
     * @param toId    to
     * @param content content
     * @return resp
     */
    default MsgSendResp sendText(String fromId, String toId, String content) {
        TextMessage message = new TextMessage();
        message.setText(content);
        return sendMessage(fromId, toId, MsgTypeEnum.TIMTextElem, message);
    }

    /**
     * 发送自定义消息
     *
     * @param fromId  from
     * @param toId    to
     * @param message message
     * @return resp
     */
    default MsgSendResp sendCustom(String fromId, String toId, CustomMessage message) {
        return sendMessage(fromId, toId, MsgTypeEnum.TIMCustomElem, message);
    }

    /**
     * 发送自定义离线消息
     *
     * @param fromId  from
     * @param toId    to
     * @param message message
     * @param title   title
     * @return resp
     */
    default MsgSendResp sendCustomOffline(String fromId, String toId, CustomMessage message, String title) {
        return sendOfflineMessage(fromId, toId, MsgTypeEnum.TIMCustomElem, message, title, message.getDesc());
    }

    /**
     * 构建发送消息请求实体
     *
     * @param fromId  from
     * @param toId    to
     * @param type    type
     * @param content content
     * @param <T>     T
     * @return req
     */
    <T extends BaseMessage> MsgSendReq buildSendReq(String fromId, String toId, MsgTypeEnum type, T content);

    /**
     * 发送消息
     *
     * @param fromId  from
     * @param toId    to
     * @param type    type
     * @param content content
     * @param <T>     T
     * @return resp
     */
    <T extends BaseMessage> MsgSendResp sendMessage(String fromId, String toId, MsgTypeEnum type, T content);

    /**
     * 发送离线消息
     *
     * @param fromId    from
     * @param toId      to
     * @param type      type
     * @param content   content
     * @param channelId channel
     * @param title     title
     * @param desc      desc
     * @param ext       ext
     * @param <T>       T
     * @return resp
     */
    <T extends BaseMessage> MsgSendResp sendOfflineMessage(String fromId, String toId, MsgTypeEnum type, T content, String channelId, String title, String desc, String ext);

    /**
     * 发送离线消息
     *
     * @param fromId  from
     * @param toId    to
     * @param type    type
     * @param content content
     * @param title   title
     * @param desc    desc
     * @param <T>     T
     * @return resp
     */
    default <T extends BaseMessage> MsgSendResp sendOfflineMessage(String fromId, String toId, MsgTypeEnum type, T content, String title, String desc) {
        return sendOfflineMessage(fromId, toId, type, content, "handsome", title, desc, JsonUtils.toJson(content));
    }

    /**
     * 获取业务ID
     *
     * @param imId     imId
     * @param defValue 默认值
     * @return 业务ID
     */
    default Long businessId(String imId, Long defValue) {
        if (StrUtil.isBlank(imId)) {
            return defValue;
        }
        String id = imId.replaceAll("^.*?(\\d+).*$", "$1");
        return StrUtil.isBlank(id) ? defValue : Long.parseLong(id);
    }

    /**
     * 获取业务ID
     *
     * @param imId imId
     * @return 业务Id
     */
    default Long businessId(String imId) {
        return businessId(imId, 0L);
    }

    /**
     * 生成IM ID
     *
     * @param businessId 业务ID
     * @return ImID
     */
    String imId(String businessId);

    /**
     * 生成IM ID
     *
     * @param id     业务ID
     * @param suffix 后缀
     * @return ImID
     */
    default String imId(Long id, String suffix) {
        return imId(String.format("%d%s", id, suffix));
    }

    /**
     * 生成IM ID
     *
     * @param id 业务ID
     * @return ImID
     */
    default String imId(Long id) {
        return imId(id, Constants.STR_EMPTY);
    }
}
