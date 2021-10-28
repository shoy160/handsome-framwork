package cn.handsome.demo.web.config;

import cn.handsome.demo.DemoConstants;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import cn.handsome.web.swagger.BaseSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends BaseSwaggerConfig {
    private String getPackage(String name) {
        return String.format("%s.%s", DemoConstants.REST_PACKAGE, name);
    }

    @Bean
    public Docket appDocket() {
        return getDocket("APP - xx", DemoConstants.VERSION, getPackage("app"), "app");
    }

    @Bean
    public Docket manageDocket() {
        return getDocket("管理后台 - xx", DemoConstants.VERSION, getPackage("manage"), "manage");
    }

    @Bean
    public Docket merchantDocket() {
        return getDocket("商户 - xx", DemoConstants.VERSION, getPackage("merchant"), "merchant");
    }
}
