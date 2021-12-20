package cn.handsome.web.config;

import cn.handsome.core.Constants;
import cn.handsome.core.WorkerIdSolver;
import cn.handsome.core.cache.Cache;
import cn.handsome.core.http.HttpClientFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
}
