package cn.handsome.rabbit.config;

import cn.handsome.rabbit.domain.RabbitRoute;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shoy
 * @date 2021/8/24
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "handsome.rabbit")
public class HandsomeRabbitProperties {
    /**
     * 默认交换机
     */
    private String exchange = "handsome.topic";
    /**
     * 默认延迟交换机
     */
    private String delayExchange = "handsome.delay";
    /**
     * 默认死信队列交换机
     */
    private String dlxExchange = "handsome.dlx";

    /**
     * 队列路由
     */
    private RabbitRoute[] routes;
}
