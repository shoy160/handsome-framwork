package cn.handsome.demo.web.event;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sdk.im.ImClient;
import cn.handsome.sdk.im.ImSimplifyClient;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import cn.handsome.sdk.im.model.message.TextMessage;
import cn.handsome.sdk.im.model.request.group.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.group.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author shoy
 * @date 2021/8/30
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class ImCommandLineRunner implements CommandLineRunner {
    private final ImClient imClient;
    private final ImSimplifyClient imSimplify;

    private List<GroupMemberResp> getMembers(String groupId, int size) {
        GroupMemberInfoReq req = new GroupMemberInfoReq();
        req.setGroupId(groupId);
        req.setLimit(size);
        req.setOffset(0);
        req.setMemberInfoFilter(Arrays.asList("Role", "JoinTime", "NameCard"));
        GroupMemberInfoResp resp = imClient.group().memberInfo(req);
        if (resp.isSuccess()) {
            return resp.getMemberList();
        }
        return new ArrayList<>();
    }

    private void destroyGroup(String groupId) {
        RestResp resp = imSimplify.destroyGroup(groupId);
        if (!resp.isSuccess()) {
            log.warn("解散失败：{}-{}", resp.getErrorCode(), resp.getErrorInfo());
        }
    }

    private void transTypeToPublic() {
        GroupInfoList infoList = imSimplify.groups(GroupTypeEnum.Private, 100);
        List<GroupInfoResp.GroupInfo> groups = infoList.getGroups();
        log.info("find private group count:{}", groups.size());
        for (GroupInfoResp.GroupInfo info : groups) {
            String groupId = info.getGroupId();
            log.info("group:{}", groupId);
            Integer memberNum = info.getMemberNum();
            if (memberNum > 0) {
                List<GroupMemberResp> members = getMembers(groupId, memberNum);
                GroupCreateReq req = new GroupCreateReq();
//                req.setGroupId(String.format("%s_t", groupId));
                req.setName(info.getName());
                req.setFaceUrl(info.getFaceUrl());
                req.setType(GroupTypeEnum.Public);
                req.setIntroduction(info.getIntroduction());
                req.setOwnerAccount(info.getOwnerAccount());
                req.setApplyJoinOption(ApplyJoinOptionEnum.NeedPermission);
                List<GroupMemberReq> memberReqs = new ArrayList<>();
                for (GroupMemberResp member : members) {
                    GroupMemberReq memberReq = new GroupMemberReq();
                    memberReq.setMemberAccount(member.getMemberAccount());
                    if (member.getRole() == GroupRoleEnum.Admin) {
                        memberReq.setRole(member.getRole());
                    }
                    memberReqs.add(memberReq);
                }
                req.setMemberList(memberReqs);
                GroupCreateResp resp = imClient.group().create(req);
                if (resp.isSuccess()) {
                    //发送系统通知
                    GroupNotificationSendReq sendReq = new GroupNotificationSendReq();
                    sendReq.setGroupId(resp.getGroupId());
                    sendReq.setContent("群组已升级");
                    imClient.group().notificationSend(sendReq);
                    //解散重建
//                    destroyGroup(groupId);
                }
                log.info("trans group to public:{}", resp.isSuccess() ? "success" : resp.getErrorInfo());
            } else {
//                destroyGroup(groupId);
            }
        }
    }

    private List<GroupInfoResp.GroupInfo> search(Predicate<GroupInfoResp.GroupInfo> filter) {
        long next = 0L;
        int size = 50;
        List<GroupInfoResp.GroupInfo> list;
        String path = "D://groups.json";

        if (FileUtil.exist(path)) {
            String json = FileUtil.readString(path, StandardCharsets.UTF_8);
            list = JsonUtils.jsonList(json, GroupInfoResp.GroupInfo.class);
        } else {
            list = new ArrayList<>();
            while (true) {
                log.info("load groups for next:{}", next);
                GroupInfoList infoList = imSimplify.groups(null, size, next);
                List<GroupInfoResp.GroupInfo> groups = infoList.getGroups();
                if (CommonUtils.isEmpty(groups)) {
                    break;
                }
                list.addAll(groups);
                next = infoList.getNext();
            }
            FileUtil.writeString(JsonUtils.toJson(list), path, StandardCharsets.UTF_8);
        }
        if (CommonUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().filter(filter).collect(Collectors.toList());
    }

    private RestResp sendNotify(String groupId) {
        GroupNotificationSendReq sendReq = new GroupNotificationSendReq();
        sendReq.setGroupId(groupId);
        sendReq.setContent("群组已升级");
        return imClient.group().notificationSend(sendReq);
    }

    private RestResp sendMsg(String groupId, String account) {
        GroupMsgSendReq req = new GroupMsgSendReq();
        req.setGroupId(groupId);
        req.setRandom(RandomUtil.randomInt(10000, 1000000));
        GroupMsgSendReq.MsgBodyDTO body = new GroupMsgSendReq.MsgBodyDTO();
        body.setMsgType("TIMTextElem");
        TextMessage message = new TextMessage();
        message.setText("1");
        body.setMsgContent(message);
        req.setFromAccount(account);
        req.setMsgBody(Collections.singletonList(body));
        return imClient.group().msgSend(req);
    }

    @Override
    public void run(String... args) {
        //发送系统通知
        //@TGS#2ZTPGTMHP   @TGS#2OSPGTMHD  @TGS#22VPGTMHI
//        sendMsg("@TGS#22VPGTMHI","79513269836058624");
//        transTypeToPublic();
//        List<GroupInfoResp.GroupInfo> list = search(t -> t.getName().contains("长沙"));
//        log.info("长沙：{}", JsonUtils.toJson(list));
        List<GroupInfoResp.GroupInfo> search = search(t -> t.getMemberNum() > 10);
//        for (GroupInfoResp.GroupInfo info : search) {
//            destroyGroup(info.getGroupId());
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> collect = search.stream().map(t -> {
            Long lastMsgTime = t.getLastMsgTime();
            String timeFormat = format.format(new Date(lastMsgTime * 1000));
            return String.format("%s,%s,%d,%s,%d", t.getGroupId(), t.getName(), t.getMemberNum(), timeFormat, t.getNextMsgSeq());
        }).collect(Collectors.toList());
        log.info("member > 10：{}", JsonUtils.toJson(collect));
    }
}
