package cn.handsome.swagger.controller;

import cn.handsome.core.utils.CommonUtils;
import cn.handsome.swagger.config.SwaggerProperties;
import cn.handsome.swagger.config.SwaggerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shay
 * @date 2020/8/22
 */
@RestController
public class SwaggerController {
    private SecurityConfiguration securityConfiguration;
    private UiConfiguration uiConfiguration;

    @Autowired(required = false)
    public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    @Autowired(required = false)
    public void setUiConfiguration(UiConfiguration uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
    }

    private final SwaggerProperties config;

    public SwaggerController(SwaggerProperties config) {
        this.config = config;
        this.uiConfiguration = UiConfigurationBuilder.builder().copyOf(this.uiConfiguration)
                .swaggerUiBaseUrl(StringUtils.trimTrailingCharacter(config.getBaseUrl(), '/'))
                .build();
        this.securityConfiguration = SecurityConfigurationBuilder.builder().copyOf(this.securityConfiguration).build();
    }

    @GetMapping("/swagger-resources/configuration/security")
    public ResponseEntity<SecurityConfiguration> securityConfiguration() {
        return new ResponseEntity<>(this.securityConfiguration, HttpStatus.OK);
    }

    @GetMapping("/swagger-resources/configuration/ui")
    public ResponseEntity<UiConfiguration> uiConfiguration() {
        return new ResponseEntity<>(this.uiConfiguration, HttpStatus.OK);
    }

    @GetMapping("/swagger-resources")
    public ResponseEntity<List<springfox.documentation.swagger.web.SwaggerResource>> swaggerResources() {
        List<springfox.documentation.swagger.web.SwaggerResource> resources = new ArrayList<>();
        for (SwaggerResource item : config.getResources()) {
            springfox.documentation.swagger.web.SwaggerResource resource = CommonUtils.toBean(item, springfox.documentation.swagger.web.SwaggerResource.class);
            resource.setSwaggerVersion(item.getVersion());
            resources.add(resource);
        }
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
