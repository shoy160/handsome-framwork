package cn.handsome.core.policy.attempt;

import java.util.concurrent.ExecutionException;

/**
 * @author luoyong
 * @date 2023/2/8
 */
public class ResultAttempt<R> implements Attempt<R> {
    private final R result;
    private final long attemptNumber;
    private final long executeTime;

    public ResultAttempt(R result, long attemptNumber, long executeTime) {
        this.result = result;
        this.attemptNumber = attemptNumber;
        this.executeTime = executeTime;
    }

    @Override
    public R get() throws ExecutionException {
        return this.result;
    }


    @Override
    public boolean hasResult() {
        return true;
    }


    @Override
    public boolean hasException() {
        return false;
    }


    @Override
    public R getResult() throws IllegalStateException {
        return this.result;
    }


    @Override
    public Throwable getExceptionCause() throws IllegalStateException {
        throw new IllegalStateException("The attempt resulted in a result, not in an exception");
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
