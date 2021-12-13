package cn.handsome.swagger.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shay
 * @date 2020/8/22
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "handsome.swagger")
public class SwaggerProperties {
    private String baseUrl = "/";
    private List<SwaggerResource> resources;
}
