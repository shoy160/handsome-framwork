package cn.handsome.sdk.im.client.impl;

import cn.handsome.core.Context;
import cn.handsome.core.http.HttpClientFactory;
import cn.handsome.core.http.HttpClientFilter;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.sdk.im.ImClient;
import cn.handsome.sdk.im.client.*;
import cn.handsome.sdk.im.config.TencentImProperties;
import cn.handsome.sdk.im.utils.SignHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

/**
 * REST API
 *
 * @author shoy
 * @date 2021/6/17
 */
@Slf4j
@Component
public class DefaultImClientImpl implements ImClient {
    private final HttpClientFactory clientFactory;
    private final HttpClientFilter clientFilter;
    private final TencentImProperties config;
    private final static String TEST_ACCOUNT_PREFIX = "t_";
    private final static String IM_ACCOUNT_PREFIX = "@TGS";

    public DefaultImClientImpl(TencentImProperties config, RestApiFilter filter) {
        if (config == null || CommonUtils.isEmpty(config.getAppId()) || CommonUtils.isEmpty(config.getKey())) {
            throw new InvalidParameterException("IM 配置未找到");
        }
        this.config = config;
        this.clientFactory = HttpClientFactory.instance();
        this.clientFilter = filter;
    }

    @Override
    public AccountClient account() {
        return clientFactory.createT(AccountClient.class, this.clientFilter);
    }

    @Override
    public OpenImClient openIm() {
        return clientFactory.createT(OpenImClient.class, this.clientFilter);
    }

    @Override
    public GroupClient group() {
        return clientFactory.createT(GroupClient.class, this.clientFilter);
    }

    @Override
    public PushClient push() {
        return clientFactory.createT(PushClient.class, this.clientFilter);
    }

    @Override
    public SnsClient sns() {
        return clientFactory.createT(SnsClient.class, this.clientFilter);
    }

    @Override
    public String generateId(String businessId) {
        if (Context.isProd() || businessId.startsWith(TEST_ACCOUNT_PREFIX) || businessId.startsWith(IM_ACCOUNT_PREFIX)) {
            return businessId;
        }
        return String.format("%s%s", TEST_ACCOUNT_PREFIX, businessId);
    }

    @Override
    public ConfigClient config() {
        return clientFactory.createT(ConfigClient.class, this.clientFilter);
    }

    @Override
    public String userSig(String userId, long expire) {
        SignHelper helper = new SignHelper(config.getAppId(), config.getKey());
        return helper.genUserSig(userId, expire);
    }
}
