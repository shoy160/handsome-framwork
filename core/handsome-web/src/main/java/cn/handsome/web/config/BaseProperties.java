package cn.handsome.web.config;

import cn.handsome.core.Constants;
import cn.handsome.core.enums.EnumSerializerType;
import cn.handsome.core.enums.TimestampType;
import cn.handsome.core.utils.CommonUtils;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author shay
 * @date 2020/8/20
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "handsome")
public class BaseProperties {
    /**
     * Ingress Token
     */
    private String tokenKey = "Token";
    /**
     * 时间戳格式(默认:Second，可选值：None,Second,MilliSecond)
     */
    private TimestampType timestamp = TimestampType.Second;

    /**
     * 时间格式化，时间戳格式为None时生效，默认值：yyyy-MM-dd HH:mm:ss
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 枚举值序列化
     */
    private EnumSerializerType enumSerializer = EnumSerializerType.Code;

    /**
     * 长整型序列化时转为字符类型
     */
    private boolean longToString = true;

    /**
     * 是否开启空值处理
     */
    private boolean enableNullValue = false;

    /**
     * JWT Token
     */
    private String jwtTokenKey = "Jwt-Token";
    private String jwtSecret = "handsome666";
    /**
     * JWT Token时效(秒)
     */
    private Integer jwtTokenExpire = 7 * 86400;
    private String jwtPrivateKey = "";
    private String jwtPublicKey = "";
    /**
     * Token分组配置
     */
    private Map<String, TokenConfig> tokens;
    private String swaggerBasePath = "/";
    private String swaggerToken = "";
    private boolean enableCors = true;
    private String corsPath = "/**";
    private String corsMethods = "GET,POST,PUT,PATCH,DELETE,OPTIONS";
    private String corsOrigin = "*";
    private String corsHeaders = "Token,Jwt-Token,Language,Content-Type,Authorization,Referer";
    private Date epochDate;
    private int datacenterId = 1;
    /**
     * 工作节点需动态根据节点生成
     */
    @Deprecated
    private int workerId = 1;

    public TokenConfig groupConfig(String group) {
        if (CommonUtils.isEmpty(group) || CommonUtils.isEmpty(this.tokens) || !this.tokens.containsKey(group)) {
            TokenConfig tokenConfig = new TokenConfig();
            tokenConfig.setKey(this.jwtTokenKey);
            tokenConfig.setSecret(this.jwtSecret);
            tokenConfig.setExpire(this.jwtTokenExpire);
            tokenConfig.setPrivateKey(this.jwtPrivateKey);
            tokenConfig.setPrivateKey(this.jwtPublicKey);
            return tokenConfig;
        }
        TokenConfig config = this.tokens.get(group);
        if (StrUtil.isBlank(config.getKey())) {
            config.setKey(this.jwtTokenKey);
        }
        if (StrUtil.isBlank(config.getSecret())) {
            config.setSecret(this.jwtSecret);
        }
        if (StrUtil.isBlank(config.getPrivateKey())) {
            config.setPrivateKey(this.jwtPrivateKey);
        }
        if (StrUtil.isBlank(config.getPublicKey())) {
            config.setPublicKey(this.jwtPublicKey);
        }
        if (null == config.getExpire()) {
            config.setExpire(this.jwtTokenExpire);
        }
        return config;
    }

    public TokenConfig groupConfig() {
        return groupConfig(Constants.STR_EMPTY);
    }

    @Getter
    @Setter
    public static class TokenConfig {
        /**
         * Token键
         */
        private String key;
        /**
         * Token密钥
         */
        private String secret;
        /**
         * RSA私钥
         */
        private String privateKey;
        /**
         * RSA公钥
         */
        private String publicKey;
        /**
         * Token时效(秒)
         */
        private Integer expire;
    }
}
