package cn.handsome.sdk.im.test;

import cn.handsome.core.lang.Func;
import cn.handsome.sdk.im.client.SnsClient;
import cn.handsome.sdk.im.model.request.sns.FriendAddReq;
import cn.handsome.sdk.im.model.request.sns.FriendGetReq;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.test.base.RestClientTest;
import org.junit.Test;

import java.util.Collections;

/**
 * @author shoy
 * @date 2021/6/18
 */
public class SnsClientTest extends RestClientTest {
    private void request(Func<RestResp, SnsClient> func) {
        SnsClient account = restClient.sns();
        RestResp resp = func.invoke(account);
        printLog(resp);
    }

    @Test
    public void addFriendTest() {
        request(client -> {
            FriendAddReq req = new FriendAddReq();
            req.setFromAccount("r1001");
            FriendAddReq.FriendItem item = new FriendAddReq.FriendItem();
            item.setToAccount("r1002");
            item.setSource("Android");
            item.setRemark("1002");
            item.setWording("我是1001");
            req.setItems(Collections.singletonList(item));
            return client.addFriend(req);
        });
    }

    @Test
    public void getFriendsTest() {
        request(client -> {
            FriendGetReq req = new FriendGetReq();
            req.setFromAccount("r1001");
            return client.getFriends(req);
        });
    }
}
