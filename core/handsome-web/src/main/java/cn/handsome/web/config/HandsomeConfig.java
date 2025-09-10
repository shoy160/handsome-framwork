package cn.handsome.web.config;

import cn.handsome.core.Constants;
import cn.handsome.core.WorkerIdSolver;
import cn.handsome.core.cache.Cache;
import cn.handsome.core.http.HttpClientFactory;
import cn.handsome.core.security.DefaultTokenSolver;
import cn.handsome.core.security.TokenSolver;
import cn.handsome.core.security.TokenVerify;
import cn.handsome.core.session.Session;
import cn.handsome.core.session.TenantSolver;
import cn.handsome.core.utils.IdWorker;
import cn.handsome.web.security.AuthContext;
import cn.handsome.web.security.HandsomeTokenVerify;
import cn.handsome.web.security.ServletSession;
import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author shay
 * @date 2021/3/4
 */
@Slf4j
@Configuration
public class HandsomeConfig {

    private Cache<String, String> cache;

    @Autowired(required = false)
    public void setCache(Cache<String, String> cache) {
        this.cache = cache;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientFactory httpClientFactory() {
        return HttpClientFactory.instance();
    }

    @Bean
    @ConditionalOnMissingBean
    public Session getSession(TenantSolver tenantSolver) {
        return new ServletSession(tenantSolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenVerify tokenVerify() {
        return new HandsomeTokenVerify(cache);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantSolver tenantSolver() {
        return () -> {
            HttpServletRequest request = AuthContext.getRequest();
            if (request == null) {
                return null;
            }
            return request.getParameter(Constants.HEADER_TENANT_ID);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenSolver tokenSolver() {
        return new DefaultTokenSolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkerIdSolver defaultWorkerId(BaseProperties config) {
        return () -> IdWorker.generateWorkerId(config.getDatacenterId(), ~(-1L << 5));
    }


    @Bean
    @ConditionalOnMissingBean
    public Snowflake snowflakeBean(BaseProperties config, WorkerIdSolver workerIdSolver) {
        Date epochDate = config.getEpochDate();
        long workerId = workerIdSolver.getWorkerId();
        log.info("当前机器的workId:{}", workerId);
        return new Snowflake(epochDate, workerId, config.getDatacenterId(), false);
    }

    @Bean
    public static BeanPostProcessor springfoxBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(
                    @NotNull Object bean, @NotNull String beanName
            ) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider
                        || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    Objects.requireNonNull(field).setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
