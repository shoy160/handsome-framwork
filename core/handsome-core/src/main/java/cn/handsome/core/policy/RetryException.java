package cn.handsome.core.policy;

import cn.handsome.core.policy.attempt.Attempt;
import lombok.Getter;

/**
 * @author luoyong
 * @date 2023/2/8
 */
@Getter
public final class RetryException extends Exception {
    private final int times;
    private final Attempt<?> lastFailedAttempt;

    public RetryException(int numberOfFailedAttempts, Attempt<?> lastFailedAttempt) {
        this("Retrying failed to complete successfully after " + numberOfFailedAttempts + " attempts.", numberOfFailedAttempts, lastFailedAttempt);
    }

    public RetryException(String message, int numberOfFailedAttempts, Attempt<?> lastFailedAttempt) {
        super(message, lastFailedAttempt.hasException() ? lastFailedAttempt.getExceptionCause() : null);
        this.times = numberOfFailedAttempts;
        this.lastFailedAttempt = lastFailedAttempt;
    }
}
