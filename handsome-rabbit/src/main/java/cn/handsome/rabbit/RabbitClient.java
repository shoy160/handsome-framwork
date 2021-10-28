package cn.handsome.rabbit;

import cn.handsome.rabbit.domain.RabbitRoute;
import org.springframework.amqp.core.Binding;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author shoy
 * @date 2021/8/23
 */
public interface RabbitClient {

    /**
     * 获取Binding
     *
     * @param routeKey  路由键
     * @param queueName 队列名
     * @param hasDlx    是否定义死信队列
     * @return Binding
     */
    Binding getBinding(String routeKey, String queueName, boolean hasDlx);

    /**
     * 获取延时Binding
     *
     * @param routeKey  路由键
     * @param queueName 队列名
     * @return DelayBinding
     */
    Binding getDelayBinding(String routeKey, String queueName);

    /**
     * 获取死信队列绑定
     *
     * @param routeKey  路由键
     * @param queueName 队列名
     * @return DLX Binding
     */
    Binding getDlxBinding(String routeKey, String queueName);

    /**
     * 绑定路由键和队列
     *
     * @param routeKey    路由键
     * @param queueName   事件
     * @param enableDlx   是否定义死信队列
     * @param enableDelay 是否支持延时队列
     */
    void binding(String routeKey, String queueName, boolean enableDlx, boolean enableDelay);

    /**
     * 绑定路由键和队列
     *
     * @param route route
     */
    default void binding(RabbitRoute route) {
        binding(route.getRouteKey(), route.getQueue(), route.isEnableDlx(), route.isEnableDelay());
    }

    /**
     * 绑定路由键和队列
     *
     * @param routeKey  路由键
     * @param queueName 事件
     */
    default void binding(String routeKey, String queueName) {
        binding(routeKey, queueName, true, false);
    }

    /**
     * 发送消息
     *
     * @param routeKey     路由键
     * @param event        事件
     * @param delaySeconds 延时(秒)
     * @param headers      Headers
     */
    void send(String routeKey, Object event, long delaySeconds, Map<String, Object> headers);

    /**
     * 发送消息
     *
     * @param routeKey     路由键
     * @param event        事件
     * @param delaySeconds 延时(秒)
     */
    default void send(String routeKey, Object event, long delaySeconds) {
        send(routeKey, event, delaySeconds, new TreeMap<>());
    }

    /**
     * 发送消息
     *
     * @param routeKey  路由键
     * @param event     事件
     * @param delayTime 时间
     * @param timeUnit  时间单位
     */
    default void send(String routeKey, Object event, long delayTime, TimeUnit timeUnit) {
        long seconds = timeUnit.toSeconds(delayTime);
        send(routeKey, event, seconds);
    }

    /**
     * 发送消息
     *
     * @param routeKey 路由键
     * @param event    事件
     * @param delayAt  时间
     * @param headers  Headers
     */
    default void send(String routeKey, Object event, Date delayAt, Map<String, Object> headers) {
        long seconds = (delayAt.getTime() - System.currentTimeMillis()) / 1000;
        send(routeKey, event, seconds, headers);
    }

    /**
     * 发送消息
     *
     * @param routeKey 路由键
     * @param event    事件
     * @param delayAt  时间
     */
    default void send(String routeKey, Object event, Date delayAt) {
        send(routeKey, event, delayAt, null);
    }

    /**
     * 发送消息
     *
     * @param routeKey 路由键
     * @param event    事件
     * @param headers  Headers
     */
    default void send(String routeKey, Object event, Map<String, Object> headers) {
        send(routeKey, event, 0L, headers);
    }

    /**
     * 发送消息
     *
     * @param routeKey 路由键
     * @param event    事件
     */
    default void send(String routeKey, Object event) {
        send(routeKey, event, 0L);
    }
}
