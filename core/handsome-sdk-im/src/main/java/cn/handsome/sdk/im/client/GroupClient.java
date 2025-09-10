package cn.handsome.sdk.im.client;

import cn.handsome.core.http.annotation.Host;
import cn.handsome.core.http.annotation.Json;
import cn.handsome.core.http.annotation.Post;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.model.request.group.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.group.*;
import cn.handsome.sdk.im.model.request.group.*;
import cn.handsome.sdk.im.model.response.group.*;

/**
 * 群组管理
 *
 * @author shoy
 * @date 2021/6/17
 */
@Host(ImConstants.BASE_URL + "group_open_http_svc/")
public interface GroupClient {

    /**
     * 获取 App 中的所有群组
     *
     * @param req req
     * @return resp
     */
    @Post("get_appid_group_list")
    GroupListResp list(@Json GroupListReq req);

    /**
     * 创建群组
     *
     * @param req req
     * @return resp
     */
    @Post("create_group")
    GroupCreateResp create(@Json GroupCreateReq req);

    /**
     * 获取群组详细资料
     *
     * @param req req
     * @return resp
     */
    @Post("get_group_info")
    GroupInfoResp info(@Json GroupInfoReq req);

    /**
     * 群成员详情
     *
     * @param req req
     * @return resp
     */
    @Post("get_group_member_info")
    GroupMemberInfoResp memberInfo(@Json GroupMemberInfoReq req);

    /**
     * 修改群基础资料
     *
     * @param req req
     * @return resp
     */
    @Post("modify_group_base_info")
    RestResp modify(@Json GroupModifyReq req);

    /**
     * 增加成员
     *
     * @param req req
     * @return resp
     */
    @Post("add_group_member")
    GroupMemberAddResp addMember(@Json GroupMemberAddReq req);

    /**
     * 删除成员
     *
     * @param req req
     * @return resp
     */
    @Post("delete_group_member")
    RestResp deleteMember(@Json GroupMemberDeleteReq req);

    /**
     * 修改群成员资料
     *
     * @param req req
     * @return resp
     */
    @Post("modify_group_member_info")
    RestResp modifyMember(@Json GroupMemberModifyReq req);

    /**
     * 解散群组
     *
     * @param req req
     * @return resp
     */
    @Post("destroy_group")
    RestResp destroy(@Json GroupDestroyReq req);

    /**
     * 用户所加入的群组
     *
     * @param req req
     * @return resp
     */
    @Post("get_joined_group_list")
    GroupJoinedListResp userJoinedList(@Json GroupJoinedListReq req);

    /**
     * 查询用户群组身份
     *
     * @param req req
     * @return resp
     */
    @Post("get_role_in_group")
    GroupMemberRoleResp memberRole(@Json GroupMemberRoleReq req);

    /**
     * 批量禁言或取消禁言
     *
     * @param req req
     * @return resp
     */
    @Post("forbid_send_msg")
    RestResp forbiddenSet(@Json GroupForbiddenSetReq req);

    /**
     * 获取被禁用成员列表
     *
     * @param req req
     * @return resp
     */
    @Post("get_group_shutted_uin")
    GroupForbiddenListResp forbiddenList(@Json GroupForbiddenListReq req);

    /**
     * 发送普通消息
     *
     * @param req req
     * @return resp
     */
    @Post("send_group_msg")
    GroupMsgSendResp msgSend(@Json GroupMsgSendReq req);

    /**
     * 发送系统通知
     *
     * @param req req
     * @return resp
     */
    @Post("send_group_system_notification")
    RestResp notificationSend(@Json GroupNotificationSendReq req);

    /**
     * 转让群主
     *
     * @param req req
     * @return resp
     */
    @Post("change_group_owner")
    RestResp ownerChange(@Json GroupOwnerChangeReq req);

    /**
     * 撤回群消息
     *
     * @param req req
     * @return resp
     */
    @Post("group_msg_recall")
    GroupMsgRecallResp msgRecall(@Json GroupMsgRecallReq req);

    /**
     * 导入群基础资料
     *
     * @param req req
     * @return resp
     */
    @Post("import_group")
    GroupImportResp groupImport(@Json GroupImportReq req);

    /**
     * 导入群消息
     *
     * @param req req
     * @return resp
     */
    @Post("import_group_msg")
    GroupMsgImportResp msgImport(@Json GroupMsgImportReq req);

    /**
     * 导入群成员
     *
     * @param req req
     * @return resp
     */
    @Post("import_group_member")
    GroupMemberImportResp memberImport(@Json GroupMemberImportReq req);

    /**
     * 设置成员未读消息数
     *
     * @param req req
     * @return resp
     */
    @Post("set_unread_msg_num")
    RestResp memberUnreadNumSet(@Json GroupMemberUnreadNumSetReq req);

    /**
     * 撤回指定成员消息
     *
     * @param req req
     * @return resp
     */
    @Post("delete_group_msg_by_sender")
    RestResp memberMsgRecall(@Json GroupMemberMsgRecallReq req);

    /**
     * 拉取群历史消息
     *
     * @param req req
     * @return resp
     */
    @Post("group_msg_get_simple")
    GroupMsgHistoryResp msgHistory(@Json GroupMsgHistoryReq req);

    /**
     * 获取直播在线人数
     *
     * @param req req
     * @return resp
     */
    @Post("get_online_member_num")
    GroupOnlineNumResp onlineNum(@Json GroupOnlineNumReq req);
}
