package cn.handsome.core.policy.strategy;

import cn.handsome.core.policy.attempt.Attempt;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public interface WaitStrategy {
    long computeSleepTime(Attempt<?> var1);
}
