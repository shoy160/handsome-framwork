package cn.handsome.sdk.payment.config;

import cn.handsome.core.Context;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Todo
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "handsome.payment")
public class PaymentProperties {
    private String gateway;
    private String projectCode;
    private String privateKey;
}
