package cn.handsome.web.jwt;

import cn.handsome.core.security.Token;
import cn.handsome.core.security.TokenSolver;
import cn.handsome.core.security.TokenVerify;
import cn.handsome.web.config.BaseProperties;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shoy
 * @date 2021/9/27
 */
@RequiredArgsConstructor
public class JwtTokenSolver implements TokenSolver {
    private final BaseProperties config;
    private final TokenVerify tokenVerify;

    @Override
    public Token getToken(String group) {
        return JwtContext.getToken(config, group);
    }

    @Override
    public String generateToken(Token token, String group, boolean single) {
        BaseProperties.TokenConfig tokenConfig = config.groupConfig(group);
        Date expireAt = null;
        if (null != tokenConfig.getExpire() && tokenConfig.getExpire() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, tokenConfig.getExpire());
            expireAt = calendar.getTime();
            token.setExp(expireAt.getTime() / 1000);
        }
        //单点登录
        if (single) {
            tokenVerify.enableSingle(token, group);
        }
        JwtTokenBuilder jwtTokenSolver = new JwtTokenBuilder(token);
        if (StrUtil.isNotEmpty(tokenConfig.getPrivateKey())) {
            //RSA
            if (null == token.getExp()) {
                return jwtTokenSolver.jwtRsa(tokenConfig.getPrivateKey());
            }
            return jwtTokenSolver.jwtRsa(tokenConfig.getPrivateKey(), expireAt);
        } else {
            if (null == token.getExp()) {
                return jwtTokenSolver.jwt(tokenConfig.getSecret());
            }
            return jwtTokenSolver.jwt(tokenConfig.getSecret(), expireAt);
        }
    }
}
