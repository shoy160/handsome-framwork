package cn.handsome.nacos.config;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shoy
 * @date 2021/6/25
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "handsome.nacos")
public class NacosProperties {
    /**
     * 服务地址
     */
    private String serverAddr = "192.168.2.211:31871";
    /**
     * 命名空间
     */
    private String namespace = "7e58bc09-2ccf-42d9-805b-690e18dc659e";

    /**
     * 配置
     */
    private Config config;

    public NacosProperties() {
        this.config = new Config();
    }

    @Getter
    @Setter
    public static class Config {
        /**
         * 开启自动更新
         */
        private boolean autoRefresh = true;
        private String group = "DEFAULT_GROUP";
        private String type = "yaml";
    }
}
