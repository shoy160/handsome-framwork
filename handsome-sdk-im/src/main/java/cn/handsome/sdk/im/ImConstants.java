package cn.handsome.sdk.im;

/**
 * @author shoy
 * @date 2021/6/19
 */
public interface ImConstants {
    /**
     * 默认过期时间
     */
    int DEFAULT_EXPIRE = 86400;

    /**
     * 基础路径
     */
    String BASE_URL = "https://console.tim.qq.com/v4/";

    /**
     * 成功错误码
     */
    int CODE_SUCCESS = 0;

    String PARAMS_SDK_APP_ID = "sdkappid";
    String PARAMS_IDENTIFIER = "identifier";
    String PARAMS_USER_SIG = "usersig";
    String PARAMS_RANDOM = "random";
    String PARAMS_CONTENT_TYPE = "contenttype";
    String CONTENT_TYPE_JSON = "json";

    // App Defined
    String DEFINED_GROUP_TYPE = "GroupType";
    String DEFINED_REFER_ID = "ReferId";

    // Group Base
//    String Group_

    String TAG_PROFILE_ROLE = "Tag_Profile_IM_Role";
    String TAG_PROFILE_NICK = "Tag_Profile_IM_Nick";
    String TAG_PROFILE_IMAGE = "Tag_Profile_IM_IMAGE";

    /**
     * 回调命令 - 状态变更回调
     */
    String CALLBACK_COMMAND_STATE_STATECHANGE = "State.StateChange";
    /**
     * 回调命令 - 添加好友之后回调
     */
    String CALLBACK_COMMAND_SNS_FRIEND_ADD = "Sns.CallbackFriendAdd";
    /**
     * 回调命令 - 添加好友之后回调
     */
    String CALLBACK_COMMAND_SNS_FRIEND_DELETE = "Sns.CallbackFriendDelete";
    /**
     * 回调命令 - 添加黑名单之后回调
     */
    String CALLBACK_COMMAND_SNS_BLACK_LIST_ADD = "Sns.CallbackBlackListAdd";
    /**
     * 回调命令 - 删除黑名单之后回调
     */
    String CALLBACK_COMMAND_SNS_BLACK_LIST_DELETE = "Sns.CallbackBlackListDelete";
    /**
     * 回调命令 - 发单聊消息之前回调
     */
    String CALLBACK_COMMAND_C2C_BEFORE_SEND_MSG = "C2C.CallbackBeforeSendMsg";
    /**
     * 回调命令 - 发单聊消息之后回调
     */
    String CALLBACK_COMMAND_C2C_AFTER_SEND_MSG = "C2C.CallbackAfterSendMsg";
    /**
     * 回调命令 - 创建群组之前回调
     */
    String CALLBACK_COMMAND_GROUP_BEFORE_CREATE_GROUP = "Group.CallbackBeforeCreateGroup";
    /**
     * 回调命令 - 创建群组之后回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_CREATE_GROUP = "Group.CallbackAfterCreateGroup";
    /**
     * 回调命令 - 申请入群之前回调
     */
    String CALLBACK_COMMAND_GROUP_BEFORE_APPLY_JOIN_GROUP = "Group.CallbackBeforeApplyJoinGroup";
    /**
     * 回调命令 - 拉人入群之前回调
     */
    String CALLBACK_COMMAND_GROUP_BEFORE_INVITE_JOIN_GROUP = "Group.CallbackBeforeInviteJoinGroup";
    /**
     * 回调命令 - 新成员入群之后回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_NEW_MEMBER_JOIN = "Group.CallbackAfterNewMemberJoin";
    /**
     * 回调命令 - 群成员离开之后回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_MEMBER_EXIT = "Group.CallbackAfterMemberExit";
    /**
     * 回调命令 - 群内发言之前回调
     */
    String CALLBACK_COMMAND_GROUP_BEFORE_SEND_MSG = "Group.CallbackBeforeSendMsg";
    /**
     * 回调命令 - 群内发言之前回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_SEND_MSG = "Group.CallbackAfterSendMsg";
    /**
     * 回调命令 - 群组满员之后回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_GROUP_FULL = "Group.CallbackAfterGroupFull";
    /**
     * 回调命令 - 群组解散之后回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_GROUP_DESTROYED = "Group.CallbackAfterGroupDestroyed";
    /**
     * 回调命令 - 群组资料修改之后回调
     */
    String CALLBACK_COMMAND_GROUP_AFTER_GROUP_INFO_CHANGED = "Group.CallbackAfterGroupInfoChanged";
}
