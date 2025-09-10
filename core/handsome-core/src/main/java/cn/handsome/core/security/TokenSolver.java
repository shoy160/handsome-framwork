package cn.handsome.core.security;

import cn.handsome.core.Constants;

/**
 * Token 获取器
 *
 * @author shay
 * @date 2021/3/4
 */
public interface TokenSolver {

    /**
     * 获取Token
     *
     * @param group 分组
     * @return token
     */
    Token getToken(String group);

    /**
     * 生成Token凭证
     *
     * @param token  token
     * @param group  分组
     * @param single 是否开启单点登录
     * @return token凭证
     */
    String generateToken(Token token, String group, boolean single);

    /**
     * 获取Token
     *
     * @return token
     */
    default Token getToken() {
        return getToken(Constants.STR_EMPTY);
    }

    /**
     * 生成Token凭证
     *
     * @param token token
     * @param group 分组
     * @return token凭证
     */
    default String generateToken(Token token, String group) {
        return generateToken(token, group, false);
    }

    /**
     * 生成Token凭证
     *
     * @param token token
     * @return token凭证
     */
    default String generateToken(Token token) {
        return generateToken(token, Constants.STR_EMPTY, false);
    }
}
