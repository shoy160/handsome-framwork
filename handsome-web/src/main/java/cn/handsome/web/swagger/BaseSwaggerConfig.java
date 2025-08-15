package cn.handsome.web.swagger;

import cn.handsome.core.Constants;
import cn.handsome.core.enums.TimestampType;
import cn.handsome.web.config.BaseProperties;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Swagger Config
 *
 * @author shay
 * @date 2020/7/21
 */
@Configuration
public abstract class BaseSwaggerConfig {
    protected final static String EMPTY = Constants.STR_EMPTY;

    @Resource
    private BaseProperties properties;

    /**
     * 构建接口信息
     *
     * @param builder builder
     * @return builder
     */
    protected ApiInfoBuilder buildApiInfo(ApiInfoBuilder builder) {
        return builder;
    }

    /**
     * 构建接口选择
     *
     * @param builder builder
     * @return builder
     */
    protected ApiSelectorBuilder buildApiSelector(ApiSelectorBuilder builder) {
        return builder;
    }


    protected Docket customHeader(Docket docket) {
        List<RequestParameter> params = new ArrayList<>();
        RequestParameter header = new RequestParameterBuilder()
                .name("Jwt-Token")
                .description("用户凭证")
                .in(ParameterType.HEADER)
                .required(false)
                .example(new Example(properties.getSwaggerToken()))
//                .defaultValue(properties.getSwaggerToken())
                .build();
        params.add(header);
        return docket.globalRequestParameters(params);
    }

    private ApiInfo apiInfo(String title, String description, String version) {
        ApiInfoBuilder builder = new ApiInfoBuilder().title(title)
                .description(description);
        if (!StrUtil.isEmpty(version)) {
            builder.version(version);
        }
        builder = buildApiInfo(builder);
        return builder.build();
    }

    protected Docket getDocket(String apiName, String basePackage) {
        return getDocket(apiName, EMPTY, basePackage);
    }

    protected Docket getDocket(String apiName, String version, String basePackage) {
        return getDocket(apiName, version, basePackage, EMPTY);
    }

    protected Docket getGroupDocket(String apiName, String version, String basePackage) {
        return getDocket(apiName, version, basePackage, apiName);
    }

    protected Docket getDocket(String apiName, String version, String basePackage, String groupName) {
        return getDocket(apiName, String.format("%s接口文档", apiName), version, basePackage, groupName);
    }

    protected Docket getDocket(String apiName, String apiDesc, String version, String basePackage, String groupName, String... paths) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(apiName, apiDesc, version));
        if (!StrUtil.isEmpty(groupName)) {
            docket.groupName(groupName);
        }
        docket.pathMapping(properties.getSwaggerBasePath());

        ApiSelectorBuilder builder = docket
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage));
        if (ArrayUtil.isEmpty(paths)) {
            builder.paths(PathSelectors.any());
        } else {
            StringBuilder pathRegex = new StringBuilder();
            for (String path : paths) {
                pathRegex.append("(").append(path).append(")|");
            }
            builder.paths(PathSelectors.regex(pathRegex.substring(0, pathRegex.length() - 1)));
        }
        builder = buildApiSelector(builder);
        docket = builder.build();
        if (properties.getTimestamp() != TimestampType.None) {
            docket.directModelSubstitute(Date.class, Long.class);
            docket.directModelSubstitute(LocalDateTime.class, Long.class);
        }
        return customHeader(docket);
    }
}
