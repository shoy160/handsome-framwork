package cn.handsome.sdk.im.client.impl;

import cn.handsome.core.cache.Cache;
import cn.handsome.core.cache.impl.MemoryCache;
import cn.handsome.core.http.HttpClientFilter;
import cn.handsome.core.http.HttpRequest;
import cn.handsome.core.http.HttpResponse;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.config.TencentImProperties;
import cn.handsome.sdk.im.utils.SignHelper;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Rest API 过滤器
 *
 * @author shoy
 * @date 2021/6/17
 */
@Slf4j
@Component
public class RestApiFilter implements HttpClientFilter {
    private final TencentImProperties config;
    private final SignHelper signHelper;
    private Cache<String, String> cache;

    @Autowired(required = false)
    public void setCache(Cache<String, String> cache) {
        this.cache = cache;
    }

    @SuppressWarnings("all")
    public RestApiFilter(TencentImProperties config) {
        this.config = config;
        this.signHelper = new SignHelper(config.getAppId(), config.getKey());
    }

    private String getUserSig() {
        String identifier = config.getAdminId();
        if (null == this.cache) {
            cache = new MemoryCache<>("framework-sdk-im");
        }
        return cache.getOrPut(String.format("im:user_sig:%s", identifier),
                key -> signHelper.genUserSig(identifier, ImConstants.DEFAULT_EXPIRE),
                ImConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    @Override
    public void before(HttpRequest request) {
        String userSig = getUserSig();
        Map<String, Object> query = new HashMap<>(5);
        query.put(ImConstants.PARAMS_SDK_APP_ID, config.getAppId());
        query.put(ImConstants.PARAMS_IDENTIFIER, config.getAdminId());
        query.put(ImConstants.PARAMS_USER_SIG, userSig);
        query.put(ImConstants.PARAMS_RANDOM, RandomUtil.randomInt(10000, Integer.MAX_VALUE));
        query.put(ImConstants.PARAMS_CONTENT_TYPE, ImConstants.CONTENT_TYPE_JSON);
        request.setParams(query);
        Object data = request.getData();
        if (data != null) {
            if (log.isDebugEnabled()) {
                log.debug("request body -> {}", JsonUtils.toJson(data));
            }
        }
    }

    @Override
    public void after(HttpResponse response, HttpRequest request) {
        if (log.isDebugEnabled()) {
            log.debug(response.readBody());
        }
    }
}
