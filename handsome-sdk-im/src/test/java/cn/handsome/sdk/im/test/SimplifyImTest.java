package cn.handsome.sdk.im.test;

import cn.handsome.sdk.im.ImSimplifyClient;
import cn.handsome.sdk.im.client.impl.DefaultImSimplifyImpl;
import cn.handsome.sdk.im.test.base.RestClientTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shoy
 * @date 2021/8/9
 */
@Slf4j
public class SimplifyImTest extends RestClientTest {
    private final ImSimplifyClient simplifyIm;

    public SimplifyImTest() {
        super();
        simplifyIm = new DefaultImSimplifyImpl(this.restClient);
    }

    @Test
    public void memberRoleTest() {
        Long id = simplifyIm.businessId("t_12312fans");
        log.info(id.toString());
//        String t = "t_12312fans".replaceAll("^.*?(\\d+).*$", "$1");
//        log.info(t);
//        int randomInt = RandomUtil.randomInt(10000, 100000);
//        log.info(String.valueOf(randomInt));
//        boolean role = simplifyIm.isGroupMember("88981660179238912fans", "77424431911276544");
//        log.info(String.valueOf(role));
    }
}
