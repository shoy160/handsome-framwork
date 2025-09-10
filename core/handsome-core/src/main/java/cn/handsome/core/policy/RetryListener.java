package cn.handsome.core.policy;

import cn.handsome.core.policy.attempt.Attempt;

/**
 * 重试监听
 *
 * @author luoyong
 * @date 2023/2/8
 */
public interface RetryListener {
    /**
     * 重试事件
     *
     * @param attempt 尝试参数
     * @param <V>     V
     */
    <V> void onRetry(Attempt<V> attempt);
}
