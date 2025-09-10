package cn.handsome.sdk.im.client;

import cn.handsome.core.http.annotation.Host;
import cn.handsome.core.http.annotation.Json;
import cn.handsome.core.http.annotation.Post;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.model.request.openim.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.openim.*;
import cn.handsome.sdk.im.model.request.openim.*;
import cn.handsome.sdk.im.model.response.openim.*;

/**
 * 单聊消息
 *
 * @author shoy
 * @date 2021/6/17
 */
@Host(ImConstants.BASE_URL + "openim/")
public interface OpenImClient {

    /**
     * 发送单聊消息
     *
     * @param req req
     * @return resp
     */
    @Post("sendmsg")
    MsgSendResp send(@Json MsgSendReq req);

    /**
     * 批量发送单聊消息
     *
     * @param req req
     * @return resp
     */
    @Post("batchsendmsg")
    MsgBatchSendResp batchSend(@Json MsgBatchSendReq req);

    /**
     * 导入单聊消息
     *
     * @param req req
     * @return resp
     */
    @Post("importmsg")
    MsgImportResp importMsg(@Json MsgImportReq req);

    /**
     * 查询单聊消息
     *
     * @param req req
     * @return resp
     */
    @Post("admin_getroammsg")
    MsgGetResp getMsg(@Json MsgGetReq req);

    /**
     * 撤回单聊消息
     *
     * @param req req
     * @return resp
     */
    @Post("admin_msgwithdraw")
    RestResp recallMsg(@Json MsgRecallReq req);

    /**
     * 设置单聊消息已读
     *
     * @param req req
     * @return resp
     */
    @Post("admin_set_msg_read")
    RestResp setRead(@Json MsgSetReadReq req);

    /**
     * 查询单聊未读计数
     *
     * @param req req
     * @return resp
     */
    @Post("get_c2c_unread_msg_num")
    MsgUnreadNumResp unreadNum(@Json MsgUnreadNumReq req);
}
