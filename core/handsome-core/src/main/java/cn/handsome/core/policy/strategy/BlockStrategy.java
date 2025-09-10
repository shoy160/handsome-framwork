package cn.handsome.core.policy.strategy;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public interface BlockStrategy {
    void block(long var1) throws InterruptedException;
}
