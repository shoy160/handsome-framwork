package cn.handsome.core.policy.attempt;

import java.util.concurrent.ExecutionException;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public class ExceptionAttempt<R> implements Attempt<R> {
    private final ExecutionException e;
    private final long attemptNumber;
    private final long executeTime;

    public ExceptionAttempt(Throwable cause, long attemptNumber, long executeTime) {
        this.e = new ExecutionException(cause);
        this.attemptNumber = attemptNumber;
        this.executeTime = executeTime;
    }

    @Override
    public R get() throws ExecutionException {
        throw this.e;
    }

    @Override
    public boolean hasResult() {
        return false;
    }

    @Override
    public boolean hasException() {
        return true;
    }

    @Override
    public R getResult() throws IllegalStateException {
        throw new IllegalStateException("The attempt resulted in an exception, not in a result");
    }

    @Override
    public Throwable getExceptionCause() throws IllegalStateException {
        return this.e.getCause();
    }

    @Override
    public long getAttemptNumber() {
        return this.attemptNumber;
    }

    @Override
    public long getExecuteTime() {
        return this.executeTime;
    }
}