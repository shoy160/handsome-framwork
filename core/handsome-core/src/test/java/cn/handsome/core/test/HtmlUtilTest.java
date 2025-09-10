package cn.handsome.core.test;

import cn.handsome.core.utils.HtmlUtil;
import cn.handsome.core.utils.IdWorker;
import cn.handsome.core.utils.IdentityUtils;
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
        log.info("value:{}", (char) 97);
        final int radix = 17;
        long id = 1481164985421418496L;
        StringBuilder builder = new StringBuilder();
        do {
            long l = id % radix;
            int code = (int) (l + 87);
            builder.append(l > 9 ? String.valueOf((char) code) : String.valueOf(l));
            id = id / radix;
        } while (id > 0);
        log.info(builder.reverse().toString());
        log.info(Long.toString(1481164985421418496L, radix));

//        String html = IdentityUtils.string16Id(); //HtmlUtil.prettyH5("hhahh");
//        log.info(html);
    }

    @Test
    public void idTest() {
        log.info("max:{}", ~(-1L << 5));
        log.info("workId:{}", IdWorker.generateWorkerId(1, ~(-1L << 5)));
    }
}
