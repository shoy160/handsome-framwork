package cn.handsome.core.limiter;

import java.util.concurrent.Semaphore;

/**
 * 令牌桶限流
 *
 * @author luoyong
 * @date 2025/3/10
 */
public class TokenBucketRateLimiter implements RateLimiter {
    /**
     * 令牌桶的总容量
     */
    private final int capacity;
    /**
     * 令牌生成速率（每秒生成的令牌数）
     */
    private final int rate;
    /**
     * 当前令牌数量
     */
    private final Semaphore tokens;
    /**
     * 上次填充令牌的时间
     */
    private long lastRefillTime;

    public TokenBucketRateLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = new Semaphore(capacity);
        this.lastRefillTime = System.currentTimeMillis();
    }

    public TokenBucketRateLimiter(int rate) {
        this(rate, rate);
    }

    @Override
    public synchronized boolean tryAcquire(int tokensToAcquire) {
        if (tokensToAcquire > this.capacity) {
            return false;
        }
        refill();
        boolean acquire = tokens.tryAcquire(tokensToAcquire);
        if (acquire) {
            return true;
        }
        try {
            // 计算还需要等待多久才能有足够的令牌
            long waitTime = calculateWaitTime(tokensToAcquire - availablePermits());
            if (waitTime > 0) {
                wait(waitTime);
                return tryAcquire(tokensToAcquire);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    @Override
    public int availablePermits() {
        return tokens.availablePermits();
    }

    /**
     * 计算还需要等待多久才能有足够的令牌
     *
     * @param missingTokens 还缺少的令牌数量
     * @return 需要等待的时间（毫秒）
     */
    private long calculateWaitTime(int missingTokens) {
        return (long) (missingTokens * 1000.0 / rate);
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastRefillTime;
        int newTokens = (int) (elapsedTime * rate / 1000);
        if (newTokens > 0) {
            int permits = Math.min(newTokens, capacity - tokens.availablePermits());
            tokens.release(permits);
            lastRefillTime = now;
        }
    }
}
