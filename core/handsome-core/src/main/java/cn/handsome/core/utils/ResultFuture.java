package cn.handsome.core.utils;

import cn.handsome.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author shay
 * @date 2020/9/18
 */
@Slf4j
public class ResultFuture<T> implements Future<T> {
    private final Object messageLock;
    private boolean done;
    private T result;
    private Throwable exception;

    public ResultFuture() {
        messageLock = new Object();
    }

    public void setResult(T result) {
        this.result = result;
        synchronized (messageLock) {
            this.done = true;
            messageLock.notifyAll();
        }
    }

    public void setException(Throwable throwable) {
        this.exception = throwable;
        synchronized (messageLock) {
            this.done = true;
            messageLock.notifyAll();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.done) {
            synchronized (messageLock) {
                if (timeout < 0) {
                    messageLock.wait();
                } else {
                    long timeoutMillis = (TimeUnit.MILLISECONDS == unit) ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                    messageLock.wait(timeoutMillis);
                }
            }
        }
        if (!this.done) {
            throw new BusinessException(504, "异步任务调用超时");
        }
        if (null != this.exception) {
            this.exception.printStackTrace();
            throw new BusinessException(500, "异步任务调用异常：" + this.exception.getMessage());
        }
        return result;
    }
}
