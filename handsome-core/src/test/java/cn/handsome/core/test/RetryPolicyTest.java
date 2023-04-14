package cn.handsome.core.test;

import cn.handsome.core.policy.RetryPolicy;
import cn.handsome.core.policy.RetryPolicyBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author luoyong
 * @date 2023/3/28
 */
public class RetryPolicyTest {

    @Test
    public void retryTest() {
        RetryPolicy<Object> policy = RetryPolicyBuilder.newBuilder()
                .build();
    }
}
