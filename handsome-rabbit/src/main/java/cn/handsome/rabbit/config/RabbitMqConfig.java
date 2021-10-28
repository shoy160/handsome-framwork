package cn.handsome.rabbit.config;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.rabbit.RabbitClient;
import cn.handsome.rabbit.domain.RabbitRoute;
import cn.handsome.rabbit.impl.RabbitClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shoy
 * @date 2021/8/23
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    private RabbitTemplate.ConfirmCallback confirmCallback() {
        return (correlationData, ack, cause) -> {
            log.info("confirm callback:{},{},{}", correlationData, ack, cause);
        };
    }

    private RabbitTemplate.ReturnsCallback returnsCallback() {
        return returnedMessage -> {
            log.info("return callback:{}", returnedMessage);
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(factory);
        template.setMandatory(true);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setConfirmCallback(confirmCallback());
        template.setReturnsCallback(returnsCallback());
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitClient rabbitClient(HandsomeRabbitProperties config, RabbitTemplate template) {
        RabbitClientImpl client = new RabbitClientImpl(template, config);
        if (CommonUtils.isNotEmpty(config.getRoutes())) {
            for (RabbitRoute route : config.getRoutes()) {
                client.binding(route);
            }
        }
        return client;
    }
}
