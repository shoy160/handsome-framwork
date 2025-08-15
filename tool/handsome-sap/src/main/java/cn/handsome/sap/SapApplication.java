package cn.handsome.sap;

import cn.handsome.core.Constants;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sap.manage.SapRfcHelper;
import cn.handsome.web.HandsomeApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shoy
 */
@Slf4j
@SpringBootApplication
@ComponentScan(value = Constants.BASE_PACKAGES)
public class SapApplication {
    public static void main(String[] args) {
        HandsomeApplication.run("handsome-sap", SapApplication.class, args);
    }

    @Slf4j
    @Component
    @ConditionalOnProperty(name = "sap.execute.test", havingValue = "true")
    @RequiredArgsConstructor
    public static class TestCommand implements CommandLineRunner {
        private final Map<String, SapRfcHelper> helpers;

        @Override
        public void run(String... args) {
            log.info("Start to Execute Test Command");
            final String conn = "vanke";
            SapRfcHelper helper = helpers.get(conn);
            Map<String, Object> params = new HashMap<>(1);
            params.put("USERNAME", "test01");
            Map<String, Object> result = helper.invokeFunc("BAPI_USER_GET_DETAIL", params, false);
            log.info("Test Command Result: {}", JsonUtils.toJson(result));
        }
    }
}
