package cn.handsome.swagger.config;

import cn.handsome.core.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shay
 * @date 2020/8/22
 */
@Slf4j
@Component
@Primary
public class SwaggerResourceProvider implements SwaggerResourcesProvider {

    @Resource
    private SwaggerProperties properties;

    @Override
    public List<springfox.documentation.swagger.web.SwaggerResource> get() {
        List<springfox.documentation.swagger.web.SwaggerResource> resources = new ArrayList<>();
        for (SwaggerResource resource : properties.getResources()) {
            springfox.documentation.swagger.web.SwaggerResource item = CommonUtils.toBean(resource, springfox.documentation.swagger.web.SwaggerResource.class);
            item.setSwaggerVersion(resource.getVersion());
            resources.add(item);
        }
        return resources;
    }
}
