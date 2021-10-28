package cn.handsome.sdk.im.test;

import cn.handsome.core.lang.Func;
import cn.handsome.sdk.im.client.OpenImClient;
import cn.handsome.sdk.im.model.request.openim.MsgGetReq;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.test.base.RestClientTest;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shoy
 * @date 2021/7/31
 */
@Slf4j
public class OpenImClientTest extends RestClientTest {
    private void request(Func<RestResp, OpenImClient> func) {
        OpenImClient client = restClient.openIm();
        RestResp resp = func.invoke(client);
        printLog(resp);
    }

    @Test
    public void getMsgTest() {
        request(client -> {
            MsgGetReq req = new MsgGetReq();
            req.setFromAccount("77403021780324353");
            req.setToAccount("78875500021420032");
            Integer minTime = Math.toIntExact(DateUtil.offsetDay(DateUtil.date(), -1).getTime() / 1000);
            Integer maxTime = Math.toIntExact(DateUtil.date().getTime() / 1000);
            req.setMinTime(minTime);
            req.setMaxTime(maxTime);
            req.setMaxCnt(20);
            return client.getMsg(req);
        });

    }
}
