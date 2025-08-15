package cn.handsome.sap.config;

import cn.handsome.sap.dto.SapRfcCredentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/8/1
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sap")
public class SapProperties {
    private Map<String, SapRfcCredentials> credentials;

    public SapProperties() {
        credentials = new HashMap<>();
    }
}
