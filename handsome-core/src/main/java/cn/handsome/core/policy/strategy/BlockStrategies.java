package cn.handsome.core.policy.strategy;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public final class BlockStrategies {
    private static final BlockStrategy THREAD_SLEEP_STRATEGY = new ThreadSleepStrategy();

    private BlockStrategies() {
    }

    public static BlockStrategy threadSleepStrategy() {
        return THREAD_SLEEP_STRATEGY;
    }

    private static class ThreadSleepStrategy implements BlockStrategy {
        private ThreadSleepStrategy() {
        }

        @Override
        public void block(long sleepTime) throws InterruptedException {
            Thread.sleep(sleepTime);
        }
    }
}
