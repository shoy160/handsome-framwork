package cn.handsome.web.config;

import cn.handsome.core.security.TokenSolver;
import cn.handsome.core.security.TokenVerify;
import cn.handsome.web.filter.AuthInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author shay
 * @date 2020/9/1
 */
@Order(value = 1)
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "cn.handsome")
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class HandsomeMvcConfig implements WebMvcConfigurer {

    private TokenSolver tokenSolver;
    private final TokenVerify tokenVerify;
    private final ObjectMapper objectMapper;
    private final BaseProperties config;
    private final static String STR_ANY = "*";
    private final static String STR_SPLIT = ",";

    @Autowired(required = false)
    public void setTokenSolver(TokenSolver tokenSolver) {
        this.tokenSolver = tokenSolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(tokenSolver, tokenVerify))
                .excludePathPatterns("/static/*", "/v2/api-docs", "/swagger-resources")
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (!config.isEnableCors()) {
            return;
        }
        String corsPath = config.getCorsPath();
        String origin = config.getCorsOrigin();
        String[] headers = config.getCorsHeaders().split(STR_SPLIT);
        String[] methods = config.getCorsMethods().split(STR_SPLIT);
        registry.addMapping(corsPath)
                .allowCredentials(true)
                .allowedOriginPatterns(origin)
                .allowedMethods(methods)
                .allowedHeaders(headers);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(this.objectMapper);
            }
        }
    }
}
