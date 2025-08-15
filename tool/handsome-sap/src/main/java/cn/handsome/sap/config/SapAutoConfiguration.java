package cn.handsome.sap.config;

import cn.handsome.sap.manage.SapRfcHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/8/1
 */
@Configuration
@RequiredArgsConstructor
public class SapAutoConfiguration {
    private final SapProperties config;

    @Bean
    public Map<String, SapRfcHelper> sapRfcHelpers() {
        Map<String, SapRfcHelper> helpers = new HashMap<>();
        config.getCredentials().forEach((k, v) -> {
            helpers.put(k, new SapRfcHelper(k, v));
        });
        return helpers;
    }
}
