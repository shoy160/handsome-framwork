package cn.handsome.core.security;

import cn.handsome.core.enums.ResultCode;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/9/26
 */
public interface TokenVerify {
    /**
     * Token校验
     *
     * @param token       token
     * @param group       group
     * @param permissions permissions
     */
    default void verify(Token token, String group, String[] permissions) {
        boolean result = verifyPermission(token, permissions);
        if (!result) {
            throw ResultCode.UN_AUTHORIZED.exception();
        }
        result = verifyForbidden(token, group);
        if (!result) {
            throw ResultCode.REQ_REJECT.exception("账号已被禁用");
        }
        result = verifySingle(token, group);
        if (!result) {
            throw ResultCode.UN_AUTHORIZED.exception("凭证已失效,请重新登录");
        }
    }

    /**
     * 权限校验
     *
     * @param token       token
     * @param permissions permissions
     * @return result
     */
    boolean verifyPermission(Token token, String[] permissions);

    /**
     * 单点校验
     *
     * @param token token
     * @param group group
     * @return result
     */
    boolean verifySingle(Token token, String group);

    /**
     * 禁用校验
     *
     * @param token token
     * @param group group
     * @return result
     */
    boolean verifyForbidden(Token token, String group);

    /**
     * 开启单点登录
     *
     * @param token token
     * @param group group
     */
    void enableSingle(Token token, String group);

    /**
     * 登出
     *
     * @param token token
     * @param group group
     */
    void logout(Token token, String group);

    /**
     * 禁用账号
     *
     * @param id        账号ID
     * @param group     分组
     * @param reason    禁用原因
     * @param expiredAt 过期时间
     */
    void forbidden(Object id, String group, String reason, Date expiredAt);

    /**
     * 禁用账号
     *
     * @param id        账号ID
     * @param group     分组
     * @param expiredAt 过期时间
     */
    default void forbidden(Object id, String group, Date expiredAt) {
        forbidden(id, group, null, expiredAt);
    }

    /**
     * 永久禁用账号
     *
     * @param id     账号ID
     * @param group  分组
     * @param reason 封禁原因
     */
    default void forbidden(Object id, String group, String reason) {
        forbidden(id, group, reason, null);
    }

    /**
     * 永久禁用账号
     *
     * @param id    账号ID
     * @param group 分组
     */
    default void forbidden(Object id, String group) {
        forbidden(id, group, null, null);
    }
}
