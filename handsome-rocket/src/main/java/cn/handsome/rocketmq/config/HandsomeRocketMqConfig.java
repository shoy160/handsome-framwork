package cn.handsome.rocketmq.config;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.rocketmq.annotation.MqListener;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.aliyun.openservices.ons.api.bean.*;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author shoy
 * @date 2021/6/9
 */
@Slf4j
@RequiredArgsConstructor
public class HandsomeRocketMqConfig {
    private final HandsomeRocketMqProperties config;
    private final Environment environment;
    private final static String TOPIC_TEMPLATE = "${handsome.rocket.topics.%s.topic:%s}";
    private final static String TAG_TEMPLATE = "${handsome.rocket.topics.%s.tag:%s}";

    @ConditionalOnMissingBean
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        ProducerBean bean = new ProducerBean();
        bean.setProperties(config.getMqProperties());
        return bean;
    }

    @ConditionalOnMissingBean
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderProducerBean buildOrderProducer() {
        OrderProducerBean bean = new OrderProducerBean();
        bean.setProperties(config.getMqProperties());
        return bean;
    }

    private Subscription getSubscription(Class<?> listenerClass, String group) {
        MqListener annotation = listenerClass.getAnnotation(MqListener.class);
        if (annotation == null) {
            return null;
        }
        Subscription subscription = new Subscription();
        // 获取定义的Topic
        String name = environment.resolvePlaceholders(annotation.topic());
        // 获取配置模板中的topic
        String topic = environment.resolvePlaceholders(String.format(TOPIC_TEMPLATE, name, name));
        subscription.setTopic(topic);
        String tag = environment.resolvePlaceholders(annotation.tag());
        // 获取配置模板中的tag
        tag = environment.resolvePlaceholders(String.format(TAG_TEMPLATE, name, tag));
        subscription.setExpression(tag);
        log.info("start mq consumer : {}[{}]->{}", topic, tag, group);
        return subscription;
    }

    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildConsumer(MessageListener[] listeners) {
        if (CommonUtils.isEmpty(listeners)) {
            return null;
        }
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = config.getMqProperties();
        String groupId = config.getGroupId();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(config.getConsumerThread()));
        consumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<Subscription, MessageListener>();
        for (MessageListener listener : listeners) {
            Subscription subscription = getSubscription(listener.getClass(), groupId);
            if (subscription == null) {
                continue;
            }
            subscriptionTable.put(subscription, listener);
        }
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderConsumerBean buildOrderConsumer(MessageOrderListener[] listeners) {
        if (CommonUtils.isEmpty(listeners)) {
            return null;
        }
        OrderConsumerBean orderConsumerBean = new OrderConsumerBean();
        //配置文件
        Properties properties = config.getMqProperties();
        String groupId = config.getOrderGroupId();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        orderConsumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageOrderListener> subscriptionTable = new HashMap<Subscription, MessageOrderListener>();
        for (MessageOrderListener listener : listeners) {
            Subscription subscription = getSubscription(listener.getClass(), groupId);
            if (subscription == null) {
                continue;
            }
            subscriptionTable.put(subscription, listener);
        }
        orderConsumerBean.setSubscriptionTable(subscriptionTable);
        return orderConsumerBean;
    }

    @ConditionalOnMissingBean
    @SuppressWarnings("all")
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public BatchConsumerBean buildBatchConsumer(BatchMessageListener[] listeners) {
        if (CommonUtils.isEmpty(listeners)) {
            return null;
        }
        BatchConsumerBean batchConsumerBean = new BatchConsumerBean();
        //配置文件
        Properties properties = config.getMqProperties();
        String groupId = config.getGroupId();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(config.getConsumerThread()));
        batchConsumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, BatchMessageListener> subscriptionTable = new HashMap<Subscription, BatchMessageListener>();
        for (BatchMessageListener listener : listeners) {
            Subscription subscription = getSubscription(listener.getClass(), groupId);
            if (subscription == null) {
                continue;
            }
            subscriptionTable.put(subscription, listener);
        }
        //订阅多个topic如上面设置
        batchConsumerBean.setSubscriptionTable(subscriptionTable);
        return batchConsumerBean;
    }
}
