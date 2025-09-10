package cn.handsome.logger.remote.config;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author shay
 * @date 2021/4/2
 */
@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "handsome.logger")
public class RemoteLoggerProperties {

    /**
     * 是否开启,default:true
     */
    private boolean enable = false;
    /**
     * 远程地址
     */
    private String host = "127.0.0.1";
    /**
     * 远程端口
     */
    private Integer port = 8610;
    /**
     * 项目名
     */
    private String project;
    /**
     * 应用名，默认同AppName
     */
    private String appName;
    /**
     * 日志登记
     */
    private Level level = Level.WARN;

    /**
     * 队列大小
     */
    private Integer queueSize = 128;

    /**
     * 发送频率(秒)
     */
    private Integer interval = 10;
}
