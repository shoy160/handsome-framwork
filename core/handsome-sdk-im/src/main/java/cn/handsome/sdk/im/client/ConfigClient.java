package cn.handsome.sdk.im.client;

import cn.handsome.core.http.annotation.Host;
import cn.handsome.core.http.annotation.Json;
import cn.handsome.core.http.annotation.Post;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.model.request.config.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.config.*;
import cn.handsome.sdk.im.model.request.config.*;
import cn.handsome.sdk.im.model.response.config.*;

/**
 * 配置管理
 *
 * @author shoy
 * @date 2021/6/17
 */
@Host(ImConstants.BASE_URL)
public interface ConfigClient {

    /**
     * 拉取运营数据
     *
     * @param req req
     * @return resp
     */
    @Post("openconfigsvr/getappinfo")
    AppInfoResp getAppInfo(@Json AppInfoReq req);

    /**
     * 设置资料
     *
     * @param req req
     * @return resp
     */
    @Post("profile/portrait_set")
    RestResp setPortrait(@Json PortraitSetReq req);

    /**
     * 拉取资料
     *
     * @param req req
     * @return resp
     */
    @Post("profile/portrait_get")
    PortraitGetResp getPortrait(@Json PortraitGetReq req);

    /**
     * 下载最近消息记录
     *
     * @param req req
     * @return resp
     */
    @Post("open_msg_svc/get_history")
    HistoryGetResp getHistory(@Json HistoryGetReq req);

    /**
     * 获取服务器IP
     *
     * @param req req
     * @return resp
     */
    @Post("ConfigSvc/GetIPList")
    ServerIpGetResp getServerIp(@Json ServerIpGetReq req);

    /**
     * 设置全局禁言
     *
     * @param req req
     * @return resp
     */
    @Post("openconfigsvr/setnospeaking")
    RestResp setNoSpeaking(@Json NoSpeakingSetReq req);

    /**
     * 查询全局禁言
     *
     * @param req req
     * @return resp
     */
    @Post("openconfigsvr/getnospeaking")
    NoSpeakingGetResp getNoSpeaking(@Json NoSpeakingGetReq req);
}
