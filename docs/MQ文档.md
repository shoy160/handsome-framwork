### 包引用

```xml

<dependency>
    <groupId>cn.handsome.framework</groupId>
    <artifactId>handsome-mq</artifactId>
</dependency>
```

### MQ配置

```yaml
# 公网测试 已开通
# topic: sysmsg
# group: GID_basic,GID_community,GID_nightclub,GID_music_festival

handsome:
  mq:
    name-srv: xxx
    access-key: xxx
    secret-key: xxx
    group-id: GID_xxx
```

### 发送消息

```java
/** 发送消息 */
@RequiredArgsConstructor
public class XXXServiceImpl implements XXXService {
    private final ProducerBean producerBean;
    private final OrderProducerBean orderProducerBean;

    public void test() {
        // 
        Message message = new Message("sysmsg", "*", "hello".getBytes());
        // 延时 或 定时消息
        long time = System.currentTimeMillis() + 3000;
        message.setStartDeliverTime(time);
        //发送普通消息
        producerBean.send(message);
        //发送有序消息
        orderProducerBean.send(message);
    }
}
```

### 订阅消息

```java
/** 普通订阅 */
@Slf4j
@MqListener(topic = "sysmsg", tag = "*")
public class SystemMessageListener implements MessageListener {
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        System.out.println("Receive: " + message);
        log.info(BytesUtils.bytesToString(message.getBody(), null));
        try {
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}

/** 有序订阅 */
@Slf4j
@MqListener(topic = "sysmsg", tag = "*")
public class SystemMessageListener implements MessageOrderListener {
    @Override
    public OrderAction consume(Message message, ConsumeOrderContext consumeOrderContext) {
        System.out.println("Receive: " + message);
        log.info(BytesUtils.bytesToString(message.getBody(), null));
        try {
            //do something..
            return OrderAction.Success;
        } catch (Exception e) {
            //消费失败
            return Action.Suspend;
        }
    }
}

/** 批量订阅 */
@Slf4j
@MqListener(topic = "sysmsg", tag = "*")
public class SystemMessageListener implements BatchMessageListener {
    @Override
    public Action consume(List<Message> list, ConsumeContext consumeContext) {
        try {
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
```

### RabbitMQ

```xml

<dependency>
    <groupId>cn.handsome.framework</groupId>
    <artifactId>handsome-rabbit</artifactId>
</dependency>
```

```yaml
spring:
  mq:
    rabbit:
      exchange: handsome.topic
      delay-exchange: handsome.delay
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: xxx
    password: xxx
    virtual-host: xxx
    publisher-confirm-type: correlated
    listener:
      simple:
        retry:
          enabled: true
          max-attempts: 6
          max-interval: 60000ms
          initial-interval: 3000ms
          multiplier: 2
        default-requeue-rejected: false
```

```yaml
# 路由绑定
handsome:
  rabbit:
    routes:
      - routeKey: xxx
        queue: xxx
        enableDlx: true
        enableDelay: true
```

```java
// 队列监听
@Slf4j
@Component
@RabbitListener(queues = "test_queue")
public class RabbitMqListener {

    private RabbitMqListener(RabbitClient rabbitClient) {
        // 绑定路由
        rabbitClient.binding("test_key", "test_queue", true);
    }

    // 手动确认模式
    @RabbitListener(queues = "test_queue", ackMode = "MANUAL")
    public void testHandler(@Payload TestEvent event, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        log.info("Test Listener 已接收到消息：{}", JsonUtils.toJson(event));
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }

    //自动确认模式
    @RabbitListener(queues = "test_queue")
    public void otherHandler(TestEvent event) {
        log.info("Other Listener 已接收到消息：{}", JsonUtils.toJson(event));
    }
}


// 纯生成者绑定路由
@Slf4j
@Configuration
public class RabbitMqConfig {

    // 普通路由绑定
    @Bean
    Binding normalBinding(RabbitClient rabbitClient) {
        return rabbitClient.getBinding("test_key", "test_queue");
    }

    // 延时路由绑定
    @Bean
    Binding delayBinding(RabbitClient rabbitClient) {
        return rabbitClient.getDelayBinding("test_key", "test_queue");
    }
}

// 发送消息
@Component
@RequiredArgsConstructor
public class xxxService {
    private final RabbitClient rabbitClient;

    public void testSend() {
        TestEvent event = new TestEvent(msg);
        // 普通消息
        rabbitClient.send("test_key",event);
        // 延时消息
        rabbitClient.send("test_key",event,30);
    }
}
```

