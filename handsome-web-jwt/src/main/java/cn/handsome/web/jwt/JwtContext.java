package cn.handsome.web.jwt;

import cn.handsome.core.security.Token;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.web.config.BaseProperties;
import cn.handsome.web.security.AuthContext;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shoy
 * @date 2021/12/3
 */
@Slf4j
public class JwtContext {

    public static Token getToken(BaseProperties config, String group) {
        HttpServletRequest request = AuthContext.getRequest();
        if (request == null) {
            return null;
        }
        Token token;
        String headerToken = request.getHeader(config.getTokenKey());
        if (StrUtil.isEmpty(headerToken)) {
            log.debug("get token from jwt");
            BaseProperties.TokenConfig tokenConfig = config.groupConfig(group);
            String jwt = request.getHeader(tokenConfig.getKey());
            if (CommonUtils.isJwt(jwt)) {
                if (StrUtil.isNotEmpty(tokenConfig.getPublicKey())) {
                    token = JwtTokenBuilder.verifyRsa(jwt, tokenConfig.getPublicKey());
                } else {
                    String secret = tokenConfig.getSecret();
                    token = JwtTokenBuilder.verify(jwt, secret);
                }
                return token;
            }
            return null;
        }
        try {
            log.debug("get token from base64");
            String jsonToken = Base64.decodeStr(headerToken);
            token = JsonUtils.json(jsonToken, Token.class);
            return token;
        } catch (Exception ex) {
            log.warn("token解析异常:{}", ex.getLocalizedMessage());
            return null;
        }
    }
}
