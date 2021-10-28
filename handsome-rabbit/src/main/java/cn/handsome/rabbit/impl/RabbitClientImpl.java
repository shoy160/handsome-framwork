package cn.handsome.rabbit.impl;

import cn.handsome.core.lang.ActionIO;
import cn.handsome.rabbit.RabbitClient;
import cn.handsome.rabbit.config.HandsomeRabbitProperties;
import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author shoy
 * @date 2021/8/23
 */
@RequiredArgsConstructor
public class RabbitClientImpl implements RabbitClient {
    private final static String HEADER_DEAD_EXCHANGE = "x-dead-letter-exchange";
    private final static String HEADER_DEAD_ROUTING_KEY = "x-dead-letter-routing-key";
    private final static String DEAD_DELAY_QUEUE_TEMPLATE = "~delay_%s";
    private final static String DEAD_DLX_QUEUE_TEMPLATE = "~dlx_%s";
    private final RabbitTemplate rabbitTemplate;
    private final HandsomeRabbitProperties config;

    private void channelAction(ActionIO<Channel> action) {
        if (null == action) {
            return;
        }
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        try {
            action.invoke(channel);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Binding getBinding(String routeKey, String queueName, boolean hasDlx) {
        Queue queue;
        if (hasDlx) {
            Map<String, Object> args = new HashMap<>(2);
            args.put(HEADER_DEAD_EXCHANGE, config.getDlxExchange());
            args.put(HEADER_DEAD_ROUTING_KEY, routeKey);
            queue = new Queue(queueName, true, false, false, args);
        } else {
            queue = new Queue(queueName, true);
        }
        TopicExchange exchange = new TopicExchange(config.getExchange(), true, false);
        channelAction(channel -> {
            channel.exchangeDeclare(config.getExchange(), BuiltinExchangeType.TOPIC, true, false, null);
            channel.queueDeclare(queue.getActualName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), queue.getArguments());
        });
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }

    @Override
    public Binding getDlxBinding(String routeKey, String queueName) {
        DirectExchange exchange = new DirectExchange(config.getDlxExchange(), true, false);
        String dlxQueue = String.format(DEAD_DLX_QUEUE_TEMPLATE, queueName);
        Queue queue = new Queue(dlxQueue, true, false, true);
        channelAction(channel -> {
            channel.exchangeDeclare(config.getDlxExchange(), BuiltinExchangeType.DIRECT, true, false, null);
            channel.queueDeclare(queue.getActualName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), queue.getArguments());
        });
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }

    @Override
    public Binding getDelayBinding(String routeKey, String queueName) {
        DirectExchange exchange = new DirectExchange(config.getDelayExchange(), true, false);
        Map<String, Object> args = new HashMap<>(2);
        args.put(HEADER_DEAD_EXCHANGE, config.getExchange());
        args.put(HEADER_DEAD_ROUTING_KEY, routeKey);
        String delayQueue = String.format(DEAD_DELAY_QUEUE_TEMPLATE, queueName);
        Queue queue = new Queue(delayQueue, true, false, true, args);
        channelAction(channel -> {
            channel.exchangeDeclare(config.getDelayExchange(), BuiltinExchangeType.DIRECT, true, false, null);
            channel.queueDeclare(queue.getActualName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(), queue.getArguments());
        });
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }


    @Override
    public void binding(String routeKey, String queueName, boolean enableDlx, boolean enableDelay) {
        channelAction(channel -> {
            String exchange = config.getExchange();
            if (enableDelay) {
                String delayExchange = config.getDelayExchange();
                channel.exchangeDeclare(delayExchange, BuiltinExchangeType.DIRECT, true, false, null);
                Map<String, Object> args = new HashMap<>(2);
                args.put(HEADER_DEAD_EXCHANGE, exchange);
                args.put(HEADER_DEAD_ROUTING_KEY, routeKey);
                String delayQueue = String.format(DEAD_DELAY_QUEUE_TEMPLATE, queueName);
                channel.queueDeclare(delayQueue, true, false, true, args);
                channel.queueBind(delayQueue, delayExchange, routeKey);
            }
            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC, true, false, null);
            if (enableDlx) {
                //开启死信队列
                Map<String, Object> args = new HashMap<>(2);
                args.put(HEADER_DEAD_EXCHANGE, config.getDlxExchange());
                args.put(HEADER_DEAD_ROUTING_KEY, routeKey);
                channel.queueDeclare(queueName, true, false, false, args);
                //绑定死信队列
                channel.exchangeDeclare(config.getDlxExchange(), BuiltinExchangeType.DIRECT, true);
                String dlxQueue = String.format(DEAD_DLX_QUEUE_TEMPLATE, queueName);
                channel.queueDeclare(dlxQueue, true, false, false, null);
                channel.queueBind(dlxQueue, config.getDlxExchange(), routeKey);
            } else {
                channel.queueDeclare(queueName, true, false, false, null);
            }
            channel.queueBind(queueName, exchange, routeKey);
        });
    }

    @Override
    public void send(String routeKey, Object event, long delaySeconds, Map<String, Object> headers) {
        String exchange = delaySeconds > 0 ? config.getDelayExchange() : config.getExchange();
        rabbitTemplate.convertAndSend(exchange, routeKey, event, message -> {
            MessageProperties properties = message.getMessageProperties();
            if (delaySeconds > 0) {
                properties.setExpiration(String.valueOf(delaySeconds * 1000));
            }
            if (MapUtil.isNotEmpty(headers)) {
                for (String key : headers.keySet()) {
                    properties.setHeader(key, headers.get(key));
                }
            }
            return message;
        });
    }
}
