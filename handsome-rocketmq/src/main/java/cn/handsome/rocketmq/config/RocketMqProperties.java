package cn.handsome.rocketmq.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

/**
 * @author shoy
 * @date 2021/6/9
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "handsome.rocket")
public class RocketMqProperties {
    /**
     * Name Service
     */
    private String nameSrv;
    /**
     * AccessKey
     */
    private String accessKey;
    /**
     * SecretKey
     */
    private String secretKey;

    /**
     * 分组ID
     */
    private String groupId;

    /**
     * 有序分组ID
     */
    private String orderGroupId;

    /**
     * 消费者线程数
     */
    private int consumerThread = 20;

    /**
     * Topic配置
     */
    private Map<String, MqTopic> topics;

    public Properties getMqProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrv);
        return properties;
    }

    public MqTopic getTopic(String key) {
        if (topics == null || !topics.containsKey(key)) {
            return null;
        }
        return topics.get(key);
    }


    @Getter
    @Setter
    public static class MqTopic {
        /**
         * Topic
         */
        private String topic;
        /**
         * Tag
         */
        private String tag;

        /**
         * GroupID
         */
        private String groupId;
    }
}
