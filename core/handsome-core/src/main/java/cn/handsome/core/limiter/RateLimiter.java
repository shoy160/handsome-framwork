package cn.handsome.core.limiter;

/**
 * @author luoyong
 * @date 2025/3/10
 */
public interface RateLimiter {
    /**
     * 获取令牌
     *
     * @return 是否成功
     */
    default boolean tryAcquire() {
        return tryAcquire(1);
    }

    /**
     * 获取指定数量的令牌
     *
     * @param tokensToAcquire 令牌数
     * @return 是否成功
     */
    boolean tryAcquire(int tokensToAcquire);

    /**
     * 当前可用令牌
     *
     * @return 令牌数
     */
    int availablePermits();
}
