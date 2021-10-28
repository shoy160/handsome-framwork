package cn.handsome.demo.web.event;


import cn.handsome.core.utils.JsonUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.Map;

/**
 * @author shoy
 * @date 2021/8/20
 */
@Slf4j
//@Component
public class RabbitMqListener {

    @RabbitListener(queues = "test_queue", ackMode = "MANUAL")
    public void testHandler(@Payload TestEvent event, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        log.info("Test Listener 已接收到消息：{}", JsonUtils.toJson(event));
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(queues = "test_queue")
    public void otherHandler(TestEvent event) {
        log.info("Other Listener 已接收到消息：{}", JsonUtils.toJson(event));
    }
}
