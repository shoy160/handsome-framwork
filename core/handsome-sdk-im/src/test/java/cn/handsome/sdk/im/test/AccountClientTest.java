package cn.handsome.sdk.im.test;

import cn.handsome.core.lang.Func;
import cn.handsome.sdk.im.client.AccountClient;
import cn.handsome.sdk.im.model.BaseIdentifier;
import cn.handsome.sdk.im.model.BaseUserId;
import cn.handsome.sdk.im.model.request.account.AccountCheckReq;
import cn.handsome.sdk.im.model.request.account.AccountQueryReq;
import cn.handsome.sdk.im.model.request.account.AccountReq;
import cn.handsome.sdk.im.model.request.account.AccountsDeleteReq;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.test.base.RestClientTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Slf4j
public class AccountClientTest extends RestClientTest {

    private void request(Func<RestResp, AccountClient> func) {
        AccountClient account = restClient.account();
        RestResp resp = func.invoke(account);
        printLog(resp);
    }

    @Test
    public void sigTest() {
        String userSig = restClient.userSig("r1001", 60);
        log.info(userSig);
    }

    @Test
    public void importTest() {
        request(client -> {
            AccountReq req = new AccountReq();
            req.setIdentifier("t_78875500021420032");
            req.setNick("心动情报小助手");
            req.setFaceUrl("https://file.handsome.cn/user/78875500021420032/headimage.png");
            return client.accountImport(req);
        });
    }

    @Test
    public void deleteTest() {
        request(client -> {
            AccountsDeleteReq req = new AccountsDeleteReq();
            req.setItems(Arrays.asList(new BaseUserId("r1001"), new BaseUserId("r1002")));
            return client.accountDelete(req);
        });
    }

    @Test
    public void checkTest() {
        request(client -> {
            for (int i = 0; i < 2; i++) {
                AccountCheckReq req = new AccountCheckReq();
                req.setItems(Arrays.asList(new BaseUserId("r1001"), new BaseUserId("r1002")));
                client.accountCheck(req);
            }
            AccountCheckReq req = new AccountCheckReq();
            req.setItems(Arrays.asList(new BaseUserId("r1001"), new BaseUserId("r1002")));
            return client.accountCheck(req);
        });
    }

    @Test
    public void kickTest() {
        request(client -> {
            BaseIdentifier req = new BaseIdentifier("107160584885334016");
            return client.kick(req);
        });
    }

    @Test
    public void queryTest() {
        request(client -> {
            AccountQueryReq req = new AccountQueryReq();
            req.setAccounts(Arrays.asList("78875500021420032", "r1002"));
            req.setNeedDetail(1);
            return client.queryState(req);
        });
    }
}
