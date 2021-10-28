package cn.handsome.web.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shoy
 * @date 2021/6/16
 */
@Slf4j
public class CommonTest {

    @Test
    public void test() {
        Map<Long, String> map = new HashMap<>();
        String v = map.get(123L);
        log.info(v);
    }
}
