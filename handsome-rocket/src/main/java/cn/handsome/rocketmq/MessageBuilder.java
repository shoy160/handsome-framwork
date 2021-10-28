package cn.handsome.rocketmq;

import cn.handsome.core.utils.JsonUtils;
import com.aliyun.openservices.ons.api.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * MQ Message构造器
 *
 * @author shoy
 * @date 2021/6/16
 */
@Getter
@Setter
public class MessageBuilder {
    private Message message;

    /**
     * Message构建器
     *
     * @param topic topic
     * @param tag   tag
     * @param event event
     */
    public MessageBuilder(String topic, String tag, Object event) {
        byte[] bytes = null;
        if (null != event) {
            String body = JsonUtils.toJson(event);
            bytes = body.getBytes();
        }
        this.message = new Message(topic, tag, bytes);
    }

    /**
     * Message构建器
     *
     * @param topic topic
     * @param event event
     */
    public MessageBuilder(String topic, Object event) {
        this(topic, "*", event);
    }

    /**
     * Message构建器
     *
     * @param topic topic
     */
    public MessageBuilder(String topic) {
        this(topic, "*", null);
    }

    /**
     * Message构建器
     *
     * @param message message
     */
    public MessageBuilder(Message message) {
        this.message = message;
    }

    public MessageBuilder setBody(Object event) {
        byte[] bytes = null;
        if (null != event) {
            String body = JsonUtils.toJson(event);
            bytes = body.getBytes();
        }
        this.message.setBody(bytes);
        return this;
    }

    /**
     * 设置重复消费次数
     *
     * @param times 次数
     * @return builder
     */
    public MessageBuilder setTimes(int times) {
        this.message.setReconsumeTimes(times);
        return this;
    }

    /**
     * 设置延时
     *
     * @param time time
     * @param unit unit
     * @return builder
     */
    public MessageBuilder setDelay(int time, TimeUnit unit) {
        this.message.setStartDeliverTime(System.currentTimeMillis() + unit.toMillis(time));
        return this;
    }

    /**
     * 设置延时(秒钟)
     *
     * @param seconds 秒钟
     * @return builder
     */
    public MessageBuilder setDelaySeconds(int seconds) {
        return setDelay(seconds, TimeUnit.SECONDS);
    }

    /**
     * 设置延时(分钟)
     *
     * @param minutes 分钟
     * @return builder
     */
    public MessageBuilder setDelayMinutes(int minutes) {
        return setDelay(minutes, TimeUnit.MINUTES);
    }

    /**
     * 获取Message
     *
     * @return message
     */
    public Message build() {
        return this.message;
    }

    /**
     * 获取事件实体
     *
     * @param clazz clazz
     * @param <T>   T
     * @return event
     */
    public <T> T getEvent(Class<T> clazz) {
        byte[] bytes = this.message.getBody();
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String body = new String(bytes);
        if (String.class.equals(clazz)) {
            return (T) body;
        }
        return JsonUtils.json(body, clazz);
    }
}
