package cn.handsome.core.test;

import cn.handsome.core.limiter.RateLimiter;
import cn.handsome.core.limiter.TokenBucketRateLimiter;
import cn.handsome.core.logger.Logger;
import cn.handsome.core.logger.impl.DefaultLogger;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author luoyong
 * @date 2025/3/10
 */
public class RateLimiterTest {
    private final Logger logger = new DefaultLogger(null);

    @Test
    public void test() throws InterruptedException, ExecutionException {
        Future<?> future = ThreadUtil.execAsync(() -> {
            RateLimiter limiter = new TokenBucketRateLimiter(2);
            for (int i = 0; i < 20; i++) {
                if (limiter.tryAcquire()) {
                    logger.info("Acquired token.");
                } else {
                    logger.info("Rate limit exceeded.");
                }
            }
        });
        logger.info("some other action1");
        ThreadUtil.safeSleep(1500);
        logger.info("some other action2");
        future.get();
    }
}
