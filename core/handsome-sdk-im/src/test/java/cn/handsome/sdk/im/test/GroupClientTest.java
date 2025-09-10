package cn.handsome.sdk.im.test;

import cn.handsome.core.lang.Func;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sdk.im.ImClient;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.ImSimplifyClient;
import cn.handsome.sdk.im.client.GroupClient;
import cn.handsome.sdk.im.client.impl.DefaultImSimplifyImpl;
import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.ApplyJoinOptionEnum;
import cn.handsome.sdk.im.model.enums.CustomGroupTypeEnum;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import cn.handsome.sdk.im.model.message.TextMessage;
import cn.handsome.sdk.im.model.request.group.GroupCreateReq;
import cn.handsome.sdk.im.model.request.group.GroupDestroyReq;
import cn.handsome.sdk.im.model.request.group.GroupInfoReq;
import cn.handsome.sdk.im.model.request.group.GroupJoinedListReq;
import cn.handsome.sdk.im.model.request.group.GroupListReq;
import cn.handsome.sdk.im.model.request.group.GroupMemberInfoReq;
import cn.handsome.sdk.im.model.request.group.GroupModifyReq;
import cn.handsome.sdk.im.model.request.group.GroupMsgSendReq;
import cn.handsome.sdk.im.model.request.group.GroupNotificationSendReq;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.group.GroupInfoList;
import cn.handsome.sdk.im.model.response.group.GroupInfoResp;
import cn.handsome.sdk.im.model.response.group.GroupJoinedListResp;
import cn.handsome.sdk.im.test.base.RestClientTest;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Slf4j
public class GroupClientTest extends RestClientTest {

    private void request(Func<RestResp, GroupClient> func) {
        GroupClient account = restClient.group();
        RestResp resp = func.invoke(account);
        printLog(resp);
    }

    @Test
    public void listTest() {
        request(client -> {
            GroupListReq req = new GroupListReq();
            req.setType(GroupTypeEnum.Private);
            req.setLimit(15);
            req.setNext(0L);
            return client.list(req);
        });
    }

    @Test
    public void userJoinedTest() {
        request(client -> {
            GroupJoinedListReq req = new GroupJoinedListReq();
            req.setMemberAccount("78055415136522241");
            GroupJoinedListReq.ResponseFilterDTO filterDTO = new GroupJoinedListReq.ResponseFilterDTO();
            filterDTO.setGroupBaseInfoFilter(Arrays.asList("Type", "Name", "MemberNum"));
            filterDTO.setSelfInfoFilter(Arrays.asList("Role", "JoinTime"));
            req.setResponseFilter(filterDTO);
            GroupJoinedListResp resp = client.userJoinedList(req);
            // Owner
            List<GroupJoinedListResp.GroupIdListDTO> list = resp.getGroupIdList().stream().filter(t -> t.getSelfInfo().getRole().equals(GroupRoleEnum.Owner.toString()))
                    .collect(Collectors.toList());
            printLog(list);
            return resp;
        });
    }

    @Test
    public void createTest() {
        request(client -> {
            GroupCreateReq req = new GroupCreateReq();
            req.setType(GroupTypeEnum.AVChatRoom);
            req.setOwnerAccount("78055415136522241");
            req.setName("Raver直播群");
            req.setFaceUrl("https://file.handsome.cn/user/78055415136522241/c036536f674d6edf.jpeg");
            return client.create(req);
        });
    }

    @Test
    public void infoTest() {
        request(client -> {
            GroupInfoReq req = new GroupInfoReq();
            req.setGroupIdList(Collections.singletonList("@TGS#2MF3VOPHD"));
            return client.info(req);
        });
    }

    @Test
    public void memberInfoTest() {
        request(client -> {
            GroupMemberInfoReq req = new GroupMemberInfoReq();
            req.setGroupId("@TGS#2XRSRGHHH");
            return client.memberInfo(req);
        });
    }

    @Test
    public void modifyTest() {
        request(client -> {
            GroupModifyReq req = new GroupModifyReq();
            req.setGroupId("@TGS#2C225RPHE"); //@TGS#2C225RPHE @TGS#2YRUVOPH6  @TGS#2MF3VOPHD
//            req.setFaceUrl("https://file.handsome.cn/user/100968879295201280/78de6784897b7405.jpeg");
//            req.setAppDefinedData(Collections.singletonList(new KeyValue("GroupType","BrandFans")));
//            req.setApplyJoinOption(ApplyJoinOptionEnum.FreeAccess);
            List<KeyValue> data = new ArrayList<>();
            data.add(new KeyValue(ImConstants.DEFINED_GROUP_TYPE, CustomGroupTypeEnum.NightclubFans.name()));
//            data.add(new KeyValue(ImConstants.DEFINED_REFER_ID, "1380821642212024322"));
            req.setAppDefinedData(data);
            return client.modify(req);
        });
    }

    @Test
    public void destroyTest() {
        request(client -> {
            GroupDestroyReq req = new GroupDestroyReq();
            req.setGroupId("@TGS#1OPU4SMHP");
            return client.destroy(req);
        });
    }

    @Test
    public void sendNotifyTest() {
        request(client -> {
            //@TGS#1GG62SMH3   @TGS#1H5P4SMHH  @TGS#1OPU4SMHP
            //@TGS#2ZTPGTMHP   @TGS#2OSPGTMHD  @TGS#22VPGTMHI
            GroupNotificationSendReq sendReq = new GroupNotificationSendReq();
            sendReq.setGroupId("");
            sendReq.setContent("群组已升级");
            return client.notificationSend(sendReq);
        });
    }

    @Test
    public void sendMsgTest() {
        request(client -> {
            GroupMsgSendReq req = new GroupMsgSendReq();
            req.setGroupId("");
            req.setRandom(RandomUtil.randomInt(10000, 1000000));
            GroupMsgSendReq.MsgBodyDTO body = new GroupMsgSendReq.MsgBodyDTO();
            body.setMsgType("TIMTextElem");
            TextMessage message = new TextMessage();
            message.setText("1");
            body.setMsgContent(message);
            req.setFromAccount("");
            req.setMsgBody(Collections.singletonList(body));
            return client.msgSend(req);
        });
    }

    @Test
    public void updateApplyJoinOptionTest() {
        ImClient client = getClient();
        DefaultImSimplifyImpl simplify = new DefaultImSimplifyImpl(client);
        long next = 0L;
        int size = 50;

        while (true) {
            GroupInfoList infoList = simplify.groups(GroupTypeEnum.Public, size, next);
            if (infoList.getNext() <= 0 || CommonUtils.isEmpty(infoList.getGroups())) {
                break;
            }
            for (GroupInfoResp.GroupInfo groupInfo : infoList.getGroups()) {
                if (groupInfo.getApplyJoinOption() != ApplyJoinOptionEnum.FreeAccess) {
                    //更新
                    GroupModifyReq req = new GroupModifyReq();
                    req.setGroupId(groupInfo.getGroupId());
                    req.setApplyJoinOption(ApplyJoinOptionEnum.FreeAccess);
                    RestResp resp = client.group().modify(req);
                    log.info("更新群组加群审核权限：{}", resp.isSuccess() ? "success" : resp.getErrorInfo());
                }
            }
            next = infoList.getNext();
        }
    }

    @Test
    public void importMembersTest() {
        String userJson = FileUtil.readString("d:\\user_list.json", StandardCharsets.UTF_8);
        List<String> list = JsonUtils.jsonList(userJson, String.class);
        if (null == list) {
            return;
        }
        final long size = 200;
        log.info("find {} user", list.size());
        ImSimplifyClient imSimplify = new DefaultImSimplifyImpl(restClient);
        int pages = (int) Math.ceil(list.size() / (double) size);
        for (int i = 0; i < pages; i++) {
            String[] userIds = list.stream().skip(i * size).limit(size).toArray(String[]::new);
            RestResp resp = imSimplify.addGroupMember("@TGS#2OU7CNOHD", true, userIds);
            log.info(JsonUtils.toJson(resp));
        }
    }
}
