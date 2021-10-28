package cn.handsome.sdk.im.test.base;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sdk.im.ImClient;
import cn.handsome.sdk.im.client.impl.DefaultImClientImpl;
import cn.handsome.sdk.im.client.impl.RestApiFilter;
import cn.handsome.sdk.im.config.TencentImProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shoy
 * @date 2021/6/17
 */
@Slf4j
public abstract class RestClientTest {

    protected final ImClient restClient;

    protected ImClient getClient() {
        return this.restClient;
    }

    protected RestClientTest() {
        TencentImProperties config = new TencentImProperties();
        // 线上
        config.setAppId(123456L);
        config.setKey("123456");
        config.setAdminId("im-admin");
        restClient = new DefaultImClientImpl(config, new RestApiFilter(config));
    }

    protected void printLog(Object resp) {
        log.info(JsonUtils.toJson(resp));
    }
}
