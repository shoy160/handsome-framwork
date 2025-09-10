package cn.handsome.web.jwt;

import cn.handsome.core.security.TokenSolver;
import cn.handsome.core.security.TokenVerify;
import cn.handsome.web.config.BaseProperties;
import cn.handsome.web.config.HandsomeMvcConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shoy
 * @date 2021/12/3
 */
@Configuration
@AutoConfigureBefore(HandsomeMvcConfig.class)
public class JwtConfig {
    @Bean
    @SuppressWarnings("all")
    public TokenSolver getTokenSolver(BaseProperties config, TokenVerify tokenVerify) {
        return new JwtTokenSolver(config, tokenVerify);
    }
}
