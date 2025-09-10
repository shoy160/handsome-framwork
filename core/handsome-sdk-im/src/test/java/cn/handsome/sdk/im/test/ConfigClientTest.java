package cn.handsome.sdk.im.test;

import cn.handsome.core.lang.Func;
import cn.handsome.sdk.im.client.ConfigClient;
import cn.handsome.sdk.im.model.request.config.AppInfoReq;
import cn.handsome.sdk.im.model.request.config.PortraitGetReq;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.config.AppInfoResp;
import cn.handsome.sdk.im.test.base.RestClientTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author shoy
 * @date 2021/8/9
 */
@Slf4j
public class ConfigClientTest extends RestClientTest {

    private void request(Func<RestResp, ConfigClient> func) {
        ConfigClient client = restClient.config();
        RestResp resp = func.invoke(client);
        printLog(resp);
    }

    @Test
    public void appInfoTest() {
        request(client -> {
            AppInfoReq req = new AppInfoReq();
            req.setRequestField(Arrays.asList("ActiveUserNum", "RegistUserNumTotal", "GroupAllGroupNum", "GroupJoinGroupTimes"));
            AppInfoResp resp = client.getAppInfo(req);
            return resp;
        });
    }

    @Test
    public void getPortraitTest() {
        request(client -> {
            PortraitGetReq req = new PortraitGetReq();
            req.setToAccount(Collections.singletonList("104212233676034048"));
            req.setTagList(Arrays.asList("Tag_Profile_IM_Nick", "Tag_Profile_IM_Image"));
            return client.getPortrait(req);
        });
    }
}
