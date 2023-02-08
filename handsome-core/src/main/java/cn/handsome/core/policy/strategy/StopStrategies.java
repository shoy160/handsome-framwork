package cn.handsome.core.policy.strategy;

import cn.handsome.core.policy.attempt.Attempt;

import java.util.concurrent.TimeUnit;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public final class StopStrategies {
    private static final StopStrategy NEVER_STOP = new NeverStopStrategy();

    private StopStrategies() {
    }

    public static StopStrategy neverStop() {
        return NEVER_STOP;
    }

    public static StopStrategy stopAfterAttempt(int attemptNumber) {
        return new StopAfterAttemptStrategy(attemptNumber);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static StopStrategy stopAfterDelay(long delayInMillis) {
        return stopAfterDelay(delayInMillis, TimeUnit.MILLISECONDS);
    }

    public static StopStrategy stopAfterDelay(long duration, TimeUnit timeUnit) {
        return new StopAfterDelayStrategy(timeUnit.toMillis(duration));
    }

    private static final class StopAfterDelayStrategy implements StopStrategy {
        private final long maxDelay;

        public StopAfterDelayStrategy(long maxDelay) {
            this.maxDelay = maxDelay;
        }

        @Override
        public boolean shouldStop(Attempt<?> failedAttempt) {
            return failedAttempt.getExecuteTime() >= this.maxDelay;
        }
    }

    private static final class StopAfterAttemptStrategy implements StopStrategy {
        private final int maxAttemptNumber;

        public StopAfterAttemptStrategy(int maxAttemptNumber) {
            this.maxAttemptNumber = maxAttemptNumber;
        }

        @Override
        public boolean shouldStop(Attempt<?> failedAttempt) {
            return failedAttempt.getAttemptNumber() >= (long) this.maxAttemptNumber;
        }
    }

    private static final class NeverStopStrategy implements StopStrategy {
        private NeverStopStrategy() {
        }

        @Override
        public boolean shouldStop(Attempt<?> failedAttempt) {
            return false;
        }
    }
}
