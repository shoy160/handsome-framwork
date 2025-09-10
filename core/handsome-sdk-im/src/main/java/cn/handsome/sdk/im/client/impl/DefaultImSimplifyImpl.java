package cn.handsome.sdk.im.client.impl;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.ImClient;
import cn.handsome.sdk.im.ImSimplifyClient;
import cn.handsome.sdk.im.client.GroupClient;
import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import cn.handsome.sdk.im.model.enums.MsgTypeEnum;
import cn.handsome.sdk.im.model.enums.CustomGroupTypeEnum;
import cn.handsome.sdk.im.model.message.BaseMessage;
import cn.handsome.sdk.im.model.request.account.AccountReq;
import cn.handsome.sdk.im.model.request.config.PortraitSetReq;
import cn.handsome.sdk.im.model.request.group.*;
import cn.handsome.sdk.im.model.request.openim.MsgBodyDTO;
import cn.handsome.sdk.im.model.request.openim.MsgSendReq;
import cn.handsome.sdk.im.model.request.openim.OfflinePushInfoDTO;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.group.*;
import cn.handsome.sdk.im.model.response.openim.MsgSendResp;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shoy
 * @date 2021/8/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultImSimplifyImpl implements ImSimplifyClient {
    private final ImClient imClient;

    /**
     * 字符截断
     *
     * @param content 字符内容
     * @param length  长度
     * @return 符合长度的字符
     */
    private static String truncated(String content, int length, Charset charset) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        if (length >= content.getBytes(charset).length) {
            return content;
        }
        char[] chars = content.toCharArray();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (char ch : chars) {
            byte[] charBytes = String.valueOf(ch).getBytes(charset);
            count += charBytes.length;
            if (count > length) {
                break;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String truncated(String content, int length) {
        return truncated(content, length, StandardCharsets.UTF_8);
    }

    @Override
    public ImClient client() {
        return this.imClient;
    }

    /**
     * 创建账号
     *
     * @param id       id
     * @param nickname 昵称
     * @param faceUrl  头像
     * @param profiles 用户资料
     * @return resp
     */
    @Override
    public RestResp createAccount(String id, String nickname, String faceUrl, Map<String, Object> profiles) {
        String imId = imId(id);
        AccountReq req = new AccountReq();
        req.setIdentifier(imId);
        if (StrUtil.isNotBlank(nickname)) {
            req.setNick(nickname);
        }
        if (StrUtil.isNotBlank(faceUrl)) {
            req.setFaceUrl(faceUrl);
        }
        RestResp resp = imClient.account().accountImport(req);
        if (resp.isSuccess() && CommonUtils.isNotEmpty(profiles)) {
            PortraitSetReq portraitSetReq = new PortraitSetReq();
            portraitSetReq.setFromAccount(imId);
            List<PortraitSetReq.ProfileItemDTO> profileList = new ArrayList<>();
            for (String key : profiles.keySet()) {
                PortraitSetReq.ProfileItemDTO dto = new PortraitSetReq.ProfileItemDTO();
                dto.setTag(key);
                dto.setValue(profiles.get(key));
                profileList.add(dto);
            }
            portraitSetReq.setProfileItem(profileList);
            imClient.config().setPortrait(portraitSetReq);
        }
        return resp;
    }

    /**
     * 创建群组
     *
     * @param createReq req
     * @param groupType groupType
     * @return resp
     */
    @Override
    public GroupCreateResp createGroup(GroupCreateReq createReq, CustomGroupTypeEnum groupType) {
        List<KeyValue> definedData = createReq.getAppDefinedData();
        if (null == definedData) {
            definedData = new ArrayList<>();
        } else if (definedData.stream().noneMatch(t -> ImConstants.DEFINED_REFER_ID.equals(t.getKey()))) {
            Long referId = businessId(createReq.getGroupId());
            definedData.add(new KeyValue(ImConstants.DEFINED_REFER_ID, referId.toString()));
        }
        String groupId = imId(createReq.getGroupId());
        createReq.setGroupId(groupId);
        //字符处理
        createReq.setName(truncated(createReq.getName(), 30));
        createReq.setIntroduction(truncated(createReq.getIntroduction(), 240));
        createReq.setNotification(truncated(createReq.getNotification(), 300));
        if (StrUtil.isNotBlank(createReq.getFaceUrl()) && createReq.getFaceUrl().getBytes(StandardCharsets.UTF_8).length > 100) {
            //头像超长100字符
            log.warn("创建群组，群头像超长，已忽略：[{}]{}", groupId, createReq.getFaceUrl());
            createReq.setFaceUrl(null);
        }
        //群主
        String owner = createReq.getOwnerAccount();
        if (StrUtil.isNotBlank(owner)) {
            String ownerId = imId(owner);
            createAccount(ownerId, null, null);
            createReq.setOwnerAccount(ownerId);
        }
        if (null != groupType && definedData.stream().noneMatch(t -> ImConstants.DEFINED_GROUP_TYPE.equals(t.getKey()))) {
            definedData.add(new KeyValue(ImConstants.DEFINED_GROUP_TYPE, groupType.toString()));
        }
        createReq.setAppDefinedData(definedData);

        return imClient.group().create(createReq);
    }

    /**
     * 修改群组
     *
     * @param modifyReq req
     * @return resp
     */
    @Override
    public RestResp modifyGroup(GroupModifyReq modifyReq) {
        String groupId = imId(modifyReq.getGroupId());
        modifyReq.setGroupId(groupId);
        //字符处理
        modifyReq.setName(truncated(modifyReq.getName(), 30));
        modifyReq.setIntroduction(truncated(modifyReq.getIntroduction(), 240));
        modifyReq.setNotification(truncated(modifyReq.getNotification(), 300));
        if (StrUtil.isNotBlank(modifyReq.getFaceUrl()) && modifyReq.getFaceUrl().getBytes(StandardCharsets.UTF_8).length > 100) {
            //头像超长100字符
            log.warn("更新群组，群头像超长，已忽略：[{}]{}", groupId, modifyReq.getFaceUrl());
            modifyReq.setFaceUrl(null);
        }
        return imClient.group().modify(modifyReq);
    }

    @Override
    public GroupInfoList groups(GroupTypeEnum type, int size, long next) {
        GroupListReq listReq = new GroupListReq();
        if (null != type) {
            listReq.setType(type);
        }
        listReq.setLimit(size);
        listReq.setNext(next);
        GroupClient groupClient = imClient.group();
        GroupListResp listResp = groupClient.list(listReq);
        List<String> idList = listResp.getGroupIdList()
                .stream()
                .map(GroupListResp.GroupId::getGroupId)
                .collect(Collectors.toList());

        final int infoSize = 20;
        int page = (int) Math.ceil(idList.size() / (double) infoSize);
        List<GroupInfoResp.GroupInfo> list = new ArrayList<>();
        for (int i = 0; i < page; i++) {
            List<String> collect = idList.stream().skip(i * 20L).limit(infoSize).collect(Collectors.toList());
            GroupInfoReq infoReq = new GroupInfoReq();
            infoReq.setGroupIdList(collect);
            GroupInfoReq.Filter filter = new GroupInfoReq.Filter();
            filter.setBaseInfoFilter(Arrays.asList("Type", "Name", "FaceUrl", "Owner_Account", "MemberNum", "MaxMemberNum", "ApplyJoinOption", "CreateTime", "LastMsgTime", "NextMsgSeq"));
            infoReq.setResponseFilter(filter);
            GroupInfoResp infoResp = groupClient.info(infoReq);
            if (infoResp.isSuccess()) {
                list.addAll(infoResp.getGroupInfo());
            }
        }
        Integer count = listResp.getTotalCount();
        return new GroupInfoList(count, listResp.getNext(), list);
    }

    /**
     * 转让群主
     *
     * @param groupId 群组ID
     * @param ownerId 群主ID
     * @return resp
     */
    @Override
    public RestResp transferGroupOwner(String groupId, String ownerId) {
        GroupRoleEnum role = groupMemberRole(groupId, ownerId);
        if (role == GroupRoleEnum.Owner) {
            return new RestResp();
        }
        if (role == GroupRoleEnum.NotMember) {
            //加群
            GroupMemberAddResp resp = addGroupMember(groupId, ownerId);
            if (!resp.isSuccess()) {
                return resp;
            }
        }
        GroupOwnerChangeReq req = new GroupOwnerChangeReq();
        req.setGroupId(imId(groupId));
        req.setNewOwnerAccount(imId(ownerId));
        return imClient.group().ownerChange(req);
    }

    /**
     * 添加群组成员
     *
     * @param groupId 群组ID
     * @param silence 是否静默加入
     * @param userIds 用户IDs
     * @return resp
     */
    @Override
    public GroupMemberAddResp addGroupMember(String groupId, boolean silence, String... userIds) {
        GroupMemberAddReq req = new GroupMemberAddReq();
        req.setGroupId(imId(groupId));
        req.setSilence(silence ? 1 : 0);
        List<GroupMemberAddReq.MemberListDTO> memberList = Arrays.stream(userIds).map(t -> {
            GroupMemberAddReq.MemberListDTO dto = new GroupMemberAddReq.MemberListDTO();
            dto.setMemberAccount(imId(t));
            return dto;
        }).collect(Collectors.toList());
        req.setMemberList(memberList);
        return imClient.group().addMember(req);
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
    @Override
    public RestResp deleteGroupMember(String groupId, boolean silence, String reason, String... userIds) {
        GroupMemberDeleteReq req = new GroupMemberDeleteReq();
        req.setGroupId(imId(groupId));
        req.setSilence(silence ? 1 : 0);
        req.setReason(reason);
        List<String> memberList = Arrays.stream(userIds).map(this::imId).collect(Collectors.toList());
        req.setMemberToDelAccount(memberList);
        return imClient.group().deleteMember(req);
    }

    /**
     * 群组成员角色
     *
     * @param groupId 群组ID
     * @param userId  成员ID
     * @return role
     */
    @Override
    public GroupRoleEnum groupMemberRole(String groupId, String userId) {
        GroupMemberRoleReq req = new GroupMemberRoleReq();
        req.setGroupId(imId(groupId));
        req.setUserAccount(Collections.singletonList(imId(userId)));
        GroupMemberRoleResp resp = imClient.group().memberRole(req);
        if (resp.isSuccess()) {
            GroupMemberRoleResp.UserIdListDTO dto = resp.getUserIdList().get(0);
            return dto.getRole();
        }
        return GroupRoleEnum.NotMember;
    }

    /**
     * 解散群组
     *
     * @param groupId groupId
     * @return resp
     */
    @Override
    public RestResp destroyGroup(String groupId) {
        GroupDestroyReq req = new GroupDestroyReq();
        req.setGroupId(imId(groupId));
        return imClient.group().destroy(req);
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
    @Override
    public <T extends BaseMessage> MsgSendReq buildSendReq(String fromId, String toId, MsgTypeEnum type, T content) {
        String fromIm = imId(fromId);
        String toIm = imId(toId);
        MsgSendReq req = new MsgSendReq();
        req.setFromAccount(fromIm);
        req.setToAccount(toIm);
        req.setMsgRandom(RandomUtil.randomInt(10000, 1000000));
        //消息内容
        MsgBodyDTO msg = new MsgBodyDTO();
        msg.setMsgContent(content);
        msg.setMsgType(type);
        req.setMsgBody(Collections.singletonList(msg));
        return req;
    }

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
    @Override
    public <T extends BaseMessage> MsgSendResp sendMessage(String fromId, String toId, MsgTypeEnum type, T content) {
        MsgSendReq req = buildSendReq(fromId, toId, type, content);
        return imClient.openIm().send(req);
    }

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
     * @param <T>       T
     * @return resp
     */
    @Override
    public <T extends BaseMessage> MsgSendResp sendOfflineMessage(
            String fromId, String toId, MsgTypeEnum type, T content,
            String channelId, String title, String desc, String ext
    ) {
        MsgSendReq req = buildSendReq(fromId, toId, type, content);
        req.setSyncOtherMachine(2);
        OfflinePushInfoDTO offlinePushInfoDTO = new OfflinePushInfoDTO();
        offlinePushInfoDTO.setPushFlag(0);
        offlinePushInfoDTO.setTitle(title);
        offlinePushInfoDTO.setDesc(desc);
        offlinePushInfoDTO.setExt(ext);
        OfflinePushInfoDTO.ApnsInfoDTO apnsInfoDTO = new OfflinePushInfoDTO.ApnsInfoDTO();
        apnsInfoDTO.setTitle(title);
        apnsInfoDTO.setBadgeMode(1);
        apnsInfoDTO.setMutableContent(1);
        offlinePushInfoDTO.setApnsInfo(apnsInfoDTO);

        OfflinePushInfoDTO.AndroidInfoDTO androidInfoDTO = new OfflinePushInfoDTO.AndroidInfoDTO();
        androidInfoDTO.setVivoClassification(1);
        androidInfoDTO.setOppoChannelID(channelId);
        androidInfoDTO.setXiaoMiChannelID(channelId);
        androidInfoDTO.setHuaWeiChannelID(channelId);
        androidInfoDTO.setGoogleChannelID(channelId);
        offlinePushInfoDTO.setAndroidInfo(androidInfoDTO);
        req.setOfflinePushInfo(offlinePushInfoDTO);
        return imClient.openIm().send(req);
    }

    @Override
    public String imId(String businessId) {
        return imClient.generateId(businessId);
    }
}
