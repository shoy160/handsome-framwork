package cn.handsome.core.policy.strategy;

import cn.handsome.core.policy.attempt.Attempt;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public final class WaitStrategies {
    private static final WaitStrategy NO_WAIT_STRATEGY = new FixedWaitStrategy(0L);

    private WaitStrategies() {
    }

    public static WaitStrategy noWait() {
        return NO_WAIT_STRATEGY;
    }

    public static WaitStrategy fixedWait(long sleepTime, TimeUnit timeUnit) throws IllegalStateException {
        return new FixedWaitStrategy(timeUnit.toMillis(sleepTime));
    }

    public static WaitStrategy randomWait(long maximumTime, TimeUnit timeUnit) {
        return new RandomWaitStrategy(0L, timeUnit.toMillis(maximumTime));
    }

    public static WaitStrategy randomWait(long minimumTime, TimeUnit minimumTimeUnit, long maximumTime, TimeUnit maximumTimeUnit) {
        return new RandomWaitStrategy(minimumTimeUnit.toMillis(minimumTime), maximumTimeUnit.toMillis(maximumTime));
    }

    public static WaitStrategy incrementingWait(long initialSleepTime, TimeUnit initialSleepTimeUnit, long increment, TimeUnit incrementTimeUnit) {
        return new IncrementingWaitStrategy(initialSleepTimeUnit.toMillis(initialSleepTime), incrementTimeUnit.toMillis(increment));
    }

    public static WaitStrategy exponentialWait() {
        return new ExponentialWaitStrategy(1L, 9223372036854775807L);
    }

    public static WaitStrategy exponentialWait(long maximumTime, TimeUnit maximumTimeUnit) {
        return new ExponentialWaitStrategy(1L, maximumTimeUnit.toMillis(maximumTime));
    }

    public static WaitStrategy exponentialWait(long multiplier, long maximumTime, TimeUnit maximumTimeUnit) {
        return new ExponentialWaitStrategy(multiplier, maximumTimeUnit.toMillis(maximumTime));
    }

    public static WaitStrategy fibonacciWait() {
        return new FibonacciWaitStrategy(1L, 9223372036854775807L);
    }

    public static WaitStrategy fibonacciWait(long maximumTime, TimeUnit maximumTimeUnit) {
        return new FibonacciWaitStrategy(1L, maximumTimeUnit.toMillis(maximumTime));
    }

    public static WaitStrategy fibonacciWait(long multiplier, long maximumTime, TimeUnit maximumTimeUnit) {
        return new FibonacciWaitStrategy(multiplier, maximumTimeUnit.toMillis(maximumTime));
    }

    public static <T extends Throwable> WaitStrategy exceptionWait(Class<T> exceptionClass, Function<T, Long> function) {
        return new ExceptionWaitStrategy<>(exceptionClass, function);
    }

    public static WaitStrategy join(WaitStrategy... waitStrategies) {
        List<WaitStrategy> waitStrategyList = Arrays.stream(waitStrategies).collect(Collectors.toList());
        return new CompositeWaitStrategy(waitStrategyList);
    }

    private static final class ExceptionWaitStrategy<T extends Throwable> implements WaitStrategy {
        private final Class<T> exceptionClass;
        private final Function<T, Long> function;

        public ExceptionWaitStrategy(Class<T> exceptionClass, Function<T, Long> function) {
            this.exceptionClass = exceptionClass;
            this.function = function;
        }

        @Override
        public long computeSleepTime(Attempt<?> lastAttempt) {
            if (lastAttempt.hasException()) {
                Throwable cause = lastAttempt.getExceptionCause();
                if (this.exceptionClass.isAssignableFrom(cause.getClass())) {
                    return this.function.apply((T) cause);
                }
            }

            return 0L;
        }
    }

    private static final class CompositeWaitStrategy implements WaitStrategy {
        private final List<WaitStrategy> waitStrategies;

        public CompositeWaitStrategy(List<WaitStrategy> waitStrategies) {
            this.waitStrategies = waitStrategies;
        }

        @Override
        public long computeSleepTime(Attempt<?> failedAttempt) {
            long waitTime = 0L;

            WaitStrategy waitStrategy;
            for (
                    Iterator<WaitStrategy> iterator = this.waitStrategies.iterator();
                    iterator.hasNext();
                    waitTime += waitStrategy.computeSleepTime(failedAttempt)
            ) {
                waitStrategy = iterator.next();
            }

            return waitTime;
        }
    }

    private static final class FibonacciWaitStrategy implements WaitStrategy {
        private final long multiplier;
        private final long maximumWait;

        public FibonacciWaitStrategy(long multiplier, long maximumWait) {
            this.multiplier = multiplier;
            this.maximumWait = maximumWait;
        }

        @Override
        public long computeSleepTime(Attempt<?> failedAttempt) {
            long fib = this.fib(failedAttempt.getAttemptNumber());
            long result = this.multiplier * fib;
            if (result > this.maximumWait || result < 0L) {
                result = this.maximumWait;
            }

            return Math.max(result, 0L);
        }

        private long fib(long n) {
            if (n == 0L) {
                return 0L;
            } else if (n == 1L) {
                return 1L;
            } else {
                long prevPrev = 0L;
                long prev = 1L;
                long result = 0L;

                for (long i = 2L; i <= n; ++i) {
                    result = prev + prevPrev;
                    prevPrev = prev;
                    prev = result;
                }

                return result;
            }
        }
    }

    private static final class ExponentialWaitStrategy implements WaitStrategy {
        private final long multiplier;
        private final long maximumWait;

        public ExponentialWaitStrategy(long multiplier, long maximumWait) {
            this.multiplier = multiplier;
            this.maximumWait = maximumWait;
        }

        @Override
        public long computeSleepTime(Attempt<?> failedAttempt) {
            double exp = Math.pow(2.0D, (double) failedAttempt.getAttemptNumber());
            long result = Math.round((double) this.multiplier * exp);
            if (result > this.maximumWait) {
                result = this.maximumWait;
            }

            return Math.max(result, 0L);
        }
    }

    private static final class IncrementingWaitStrategy implements WaitStrategy {
        private final long initialSleepTime;
        private final long increment;

        public IncrementingWaitStrategy(long initialSleepTime, long increment) {
            this.initialSleepTime = initialSleepTime;
            this.increment = increment;
        }

        @Override
        public long computeSleepTime(Attempt<?> failedAttempt) {
            long result = this.initialSleepTime + this.increment * (failedAttempt.getAttemptNumber() - 1L);
            return Math.max(result, 0L);
        }
    }

    private static final class RandomWaitStrategy implements WaitStrategy {
        private static final Random RANDOM = new Random();
        private final long minimum;
        private final long maximum;

        public RandomWaitStrategy(long minimum, long maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        @Override
        public long computeSleepTime(Attempt<?> failedAttempt) {
            long t = Math.abs(RANDOM.nextLong()) % (this.maximum - this.minimum);
            return t + this.minimum;
        }
    }

    private static final class FixedWaitStrategy implements WaitStrategy {
        private final long sleepTime;

        public FixedWaitStrategy(long sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public long computeSleepTime(Attempt<?> failedAttempt) {
            return this.sleepTime;
        }
    }
}
