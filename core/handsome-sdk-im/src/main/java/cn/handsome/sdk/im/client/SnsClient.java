package cn.handsome.sdk.im.client;

import cn.handsome.core.http.annotation.Host;
import cn.handsome.core.http.annotation.Json;
import cn.handsome.core.http.annotation.Post;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.model.request.sns.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.sns.*;
import cn.handsome.sdk.im.model.request.sns.*;
import cn.handsome.sdk.im.model.response.sns.*;

/**
 * 关系链管理
 *
 * @author shoy
 * @date 2021/6/17
 */
@Host(ImConstants.BASE_URL + "sns/")
public interface SnsClient {

    /**
     * 添加好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_add")
    FriendAddResp addFriend(@Json FriendAddReq req);

    /**
     * 导入好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_import")
    FriendImportResp importFriend(@Json FriendImportReq req);

    /**
     * 更新好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_update")
    FriendUpdateResp updateFriend(@Json FriendUpdateReq req);

    /**
     * 删除好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_delete")
    FriendDeleteResp deleteFriend(@Json FriendDeleteReq req);

    /**
     * 删除所有好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_delete_all")
    RestResp deleteAllFriend(@Json FriendDeleteAllReq req);


    /**
     * 拉取好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_get")
    FriendGetResp getFriends(@Json FriendGetReq req);

    /**
     * 拉取指定好友
     *
     * @param req req
     * @return resp
     */
    @Post("friend_get_list")
    FriendGetListResp getFriendList(@Json FriendGetListReq req);

    /**
     * 添加黑名单
     *
     * @param req req
     * @return resp
     */
    @Post("black_list_add")
    BlackAddResp addBlack(@Json BlackAddReq req);

    /**
     * 删除黑名单
     *
     * @param req req
     * @return resp
     */
    @Post("black_list_delete")
    BlackDeleteResp deleteBlack(@Json BlackDeleteReq req);

    /**
     * 拉取黑名单
     *
     * @param req req
     * @return resp
     */
    @Post("black_list_get")
    BlackGetResp getBlack(@Json BlackGetReq req);

    /**
     * 校验黑名单
     *
     * @param req req
     * @return resp
     */
    @Post("black_list_check")
    BlackCheckResp checkBlack(@Json BlackCheckReq req);

    /**
     * 添加分组
     *
     * @param req req
     * @return resp
     */
    @Post("group_add")
    SnsGroupAddResp addGroup(@Json SnsGroupAddReq req);

    /**
     * 删除分组
     *
     * @param req req
     * @return resp
     */
    @Post("group_delete")
    SnsGroupDeleteResp deleteGroup(@Json SnsGroupDeleteReq req);

    /**
     * 拉取分组
     *
     * @param req req
     * @return resp
     */
    @Post("group_get")
    SnsGroupGetResp getGroup(@Json SnsGroupGetReq req);
}
