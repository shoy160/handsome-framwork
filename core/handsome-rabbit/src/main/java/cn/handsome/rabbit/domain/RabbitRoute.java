package cn.handsome.rabbit.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/8/25
 */
@Getter
@Setter
public class RabbitRoute {
    /**
     * 路由键
     */
    private String routeKey;
    /**
     * 队列
     */
    private String queue;
    /**
     * 是否开启死信队列，默认：true
     */
    private boolean enableDlx = true;
    /**
     * 是否开启延时队列,默认：true
     */
    private boolean enableDelay = true;
}
