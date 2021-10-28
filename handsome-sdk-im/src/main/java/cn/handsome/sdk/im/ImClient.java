package cn.handsome.sdk.im;

import cn.handsome.core.Constants;
import cn.handsome.sdk.im.client.*;
import cn.handsome.sdk.im.client.*;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/6/17
 */

public interface ImClient {
    /**
     * 账号管理
     *
     * @return account client
     */
    AccountClient account();

    /**
     * 单聊消息
     *
     * @return client
     */
    OpenImClient openIm();

    /**
     * 群组管理
     *
     * @return client
     */
    GroupClient group();

    /**
     * 推送管理
     *
     * @return client
     */
    PushClient push();

    /**
     * 关系链管理
     *
     * @return client
     */
    SnsClient sns();

    /**
     * 生成ID
     *
     * @param businessId 业务ID
     * @return ImID
     */
    String generateId(String businessId);

    /**
     * 生成ID
     *
     * @param id     业务ID
     * @param suffix 后缀
     * @return ImID
     */
    default String generateId(Long id, String suffix) {
        return generateId(String.format("%d%s", id, suffix));
    }

    /**
     * 生成ID
     *
     * @param id 业务ID
     * @return ImID
     */
    default String generateId(Long id) {
        return generateId(id, Constants.EMPTY_STR);
    }

    /**
     * 配置管理
     *
     * @return client
     */
    ConfigClient config();

    /**
     * 用户签名
     *
     * @param userId 用户ID
     * @param expire 过期时间(秒)
     * @return user sig
     */
    String userSig(String userId, long expire);

    /**
     * 用户签名
     *
     * @param userId   用户ID
     * @param expireAt 过期时间
     * @return user sig
     */
    default String userSig(String userId, Date expireAt) {
        long expire = (expireAt.getTime() - System.currentTimeMillis()) / 1000;
        return userSig(userId, expire);
    }

    /**
     * 用户签名
     *
     * @param userId 用户ID
     * @param expire 过期时间(秒)
     * @return user sig
     */
    default String userSig(long userId, long expire) {
        return userSig(String.valueOf(userId), expire);
    }

    /**
     * 用户签名
     *
     * @param userId   用户ID
     * @param expireAt 过期时间
     * @return user sig
     */
    default String userSig(long userId, Date expireAt) {
        return userSig(String.valueOf(userId), expireAt);
    }
}
