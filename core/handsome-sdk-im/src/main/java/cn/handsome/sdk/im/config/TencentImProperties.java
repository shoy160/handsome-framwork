package cn.handsome.sdk.im.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shoy
 * @date 2021/6/17
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "handsome.im")
public class TencentImProperties {
    /**
     * App ID
     */
    private Long appId;

    /**
     * 密钥
     */
    private String key;

    /**
     * 管理员账号
     */
    private String adminId;
}
