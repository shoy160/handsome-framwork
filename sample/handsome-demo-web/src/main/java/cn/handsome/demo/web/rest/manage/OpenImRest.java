package cn.handsome.demo.web.rest.manage;

import cn.handsome.core.Constants;
import cn.handsome.core.domain.dto.PagedDTO;
import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.demo.web.model.manage.ImGroupListVO;
import cn.handsome.demo.web.model.manage.ImGroupMemberVO;
import cn.handsome.demo.web.model.manage.ImGroupVO;
import cn.handsome.demo.web.model.manage.command.ImGroupEditCmd;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.ImClient;
import cn.handsome.sdk.im.ImSimplifyClient;
import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import cn.handsome.sdk.im.model.request.config.PortraitGetReq;
import cn.handsome.sdk.im.model.request.group.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.config.PortraitGetResp;
import cn.handsome.web.annotation.EnableAuth;
import cn.handsome.web.model.vo.PageVO;
import cn.handsome.sdk.im.model.response.group.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shoy
 * @date 2021/8/26
 */
@Slf4j
@RequiredArgsConstructor
@RestController("OpenImRest")
@RequestMapping("manage/im")
@Api(value = "OpenImRest", tags = "IM服务")
@EnableAuth(anonymous = true, group = Constants.GROUP_MANAGE)
public class OpenImRest extends BaseManageRest {
    private final ImClient imClient;
    private final ImSimplifyClient imSimplify;

    @GetMapping("group/page")
    @ApiOperation(value = "群组列表", notes = "群组列表")
    public ResultDTO<ImGroupListVO> groupPaged(
            @RequestParam(required = false) @ApiParam(value = "群组类型") GroupTypeEnum type,
            @RequestParam(required = false, defaultValue = "15")
            @ApiParam(value = "群组个数", defaultValue = "15") Integer limit,
            @RequestParam(required = false) Long next
    ) {
        if (null == next || next < 0) {
            next = 0L;
        }
        GroupInfoList infoList = imSimplify.groups(type, limit, next);
        List<ImGroupVO> list = new ArrayList<>();
        for (GroupInfoResp.GroupInfo groupInfo : infoList.getGroups()) {
            ImGroupVO item = new ImGroupVO();
            item.setId(groupInfo.getGroupId());
            item.setType(groupInfo.getType());
            item.setName(groupInfo.getName());
            item.setOwnerAccount(groupInfo.getOwnerAccount());
            item.setMemberNum(groupInfo.getMemberNum());
            item.setMaxMemberNum(groupInfo.getMaxMemberCount());
            item.setFaceUrl(groupInfo.getFaceUrl());
            item.setApplyJoinOption(groupInfo.getApplyJoinOption());
            item.setCreateTime(new Date(groupInfo.getCreateTime() * 1000));
            list.add(item);
        }
        return success(new ImGroupListVO(infoList.getTotal(), infoList.getNext(), list));
    }

    @PutMapping("group/{id}")
    @ApiOperation(value = "修改群组", notes = "修改群组")
    public ResultDTO<?> editGroup(@PathVariable String id, @RequestBody ImGroupEditCmd cmd) {
        GroupModifyReq req = toBean(cmd, GroupModifyReq.class);
        req.setGroupId(id);
        RestResp resp = imClient.group().modify(req);
        return result(resp.isSuccess(), "编辑群组失败");
    }

    @DeleteMapping("group/{id}")
    @ApiOperation(value = "解散群组", notes = "解散群组")
    public ResultDTO<?> destroyGroup(@PathVariable String id) {
        GroupDestroyReq req = new GroupDestroyReq();
        req.setGroupId(id);
        RestResp resp = imClient.group().destroy(req);
        return result(resp.isSuccess(), "解散群组失败");
    }

    @GetMapping("group/{id}/member")
    @ApiOperation(value = "群组成员", notes = "群组成员")
    public ResultDTO<PagedDTO<ImGroupMemberVO>> groupMembers(@PathVariable String id, @Valid PageVO pageVO) {
        GroupMemberInfoReq req = new GroupMemberInfoReq();
        req.setGroupId(id);
        req.setLimit(pageVO.getSize());
        req.setOffset((pageVO.getPage() - 1) * pageVO.getSize());
        req.setMemberInfoFilter(Arrays.asList("Role", "JoinTime", "NameCard"));
        GroupMemberInfoResp resp = imClient.group().memberInfo(req);
        List<ImGroupMemberVO> list = new ArrayList<>();
        List<String> idList = resp.getMemberList().stream().map(GroupMemberResp::getMemberAccount).collect(Collectors.toList());
        PortraitGetReq portraitReq = new PortraitGetReq();
        portraitReq.setToAccount(idList);
        portraitReq.setTagList(Arrays.asList(ImConstants.TAG_PROFILE_NICK));//, ImConstants.TAG_PROFILE_IMAGE
        PortraitGetResp portraitResp = imClient.config().getPortrait(portraitReq);
        if (!portraitResp.isSuccess()) {
            log.warn("拉取用户资料失败，{}:{}", portraitResp.getErrorCode(), portraitResp.getErrorInfo());
        }
        for (GroupMemberResp memberResp : resp.getMemberList()) {
            ImGroupMemberVO item = new ImGroupMemberVO();
            String account = memberResp.getMemberAccount();
            item.setAccount(account);
            item.setRole(memberResp.getRole());
            item.setNameCard(memberResp.getNameCard());
            item.setJoinTime(new Date(memberResp.getJoinTime() * 1000));
            if (portraitResp.isSuccess()) {
                Optional<PortraitGetResp.UserProfileItemDTO> first = portraitResp.getUserProfileItem().stream().filter(t -> t.getToAccount().equals(account)).findFirst();
                if (first.isPresent()) {
                    List<PortraitGetResp.UserProfileItemDTO.ProfileItemDTO> profileItem = first.get().getProfileItem();
                    for (PortraitGetResp.UserProfileItemDTO.ProfileItemDTO profileItemDTO : profileItem) {
                        if (profileItemDTO.getTag().equals(ImConstants.TAG_PROFILE_NICK)) {
                            item.setNick(profileItemDTO.getValue().toString());
                        }
                        if (profileItemDTO.getTag().equals(ImConstants.TAG_PROFILE_IMAGE)) {
                            item.setFaceUrl(profileItemDTO.getValue().toString());
                        }
                    }
                }
            }
            list.add(item);
        }
        return success(PagedDTO.paged(list, resp.getMemberNum(), pageVO.getPage(), pageVO.getSize()));
    }

    @DeleteMapping("group/{id}/member")
    @ApiOperation(value = "删除群组成员", notes = "删除群组成员")
    public ResultDTO<?> removeGroupMembers(@PathVariable String id, String accounts, boolean silence, String reason) {
        GroupMemberDeleteReq req = new GroupMemberDeleteReq();
        req.setGroupId(id);
        req.setSilence(silence ? 1 : 0);
        req.setReason(reason);
        List<String> accountList = Arrays.stream(accounts.split("[,;]")).collect(Collectors.toList());
        req.setMemberToDelAccount(accountList);
        RestResp resp = imClient.group().deleteMember(req);
        return result(resp.isSuccess(), "删除群组成员失败");
    }

    @GetMapping("group/{id}/message")
    @ApiOperation(value = "群组历史消息", notes = "群组历史消息")
    public ResultDTO<List<GroupMsgHistoryResp.RspMsgListDTO>> groupMessage(@PathVariable String id, Integer msgSeq) {
        GroupMsgHistoryReq req = new GroupMsgHistoryReq();
        req.setGroupId(id);
        req.setReqMsgNumber(10);
        req.setReqMsgSeq(msgSeq);
        GroupMsgHistoryResp resp = imClient.group().msgHistory(req);
        List<GroupMsgHistoryResp.RspMsgListDTO> rspMsgList = resp.getRspMsgList();
        return success(rspMsgList);
    }
}
