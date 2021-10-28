package cn.handsome.core.test;

import cn.handsome.core.utils.HtmlUtil;
import cn.handsome.core.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author shoy
 * @date 2021/7/8
 */
@Slf4j
public class HtmlUtilTest {
    @Test
    public void prettyTest() {
        String html = HtmlUtil.prettyH5("hhahh");
        log.info(html);
    }

    @Test
    public void idTest() {
        log.info("max:{}", ~(-1L << 5));
        log.info("workId:{}", IdWorker.generateWorkerId(1, ~(-1L << 5)));
    }
}
