package cn.handsome.core.utils;

import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.logger.LogMessage;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *
 * @author luoyong
 * @date 2025/8/1
 */
@Slf4j
public final class ExecutorUtils {
    /**
     * 获取 CPU 核心数
     */
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();
    private static AsyncTaskExecutor globalExecutor;

    public static void setGlobalExecutor(AsyncTaskExecutor executor) {
        if (Objects.nonNull(ExecutorUtils.globalExecutor)) {
            tryShutdown(ExecutorUtils.globalExecutor);
        }
        ExecutorUtils.globalExecutor = executor;
    }

    public static AsyncTaskExecutor getGlobalExecutorOrDefault() {
        if (Objects.isNull(ExecutorUtils.globalExecutor)) {
            // IO 密集型：CPU核心数 × 5~10（充分利用 CPU 空闲时间）。
            // CPU 密集型：CPU核心数 + 1（减少上下文切换）。
            return ExecutorUtils.globalExecutor = create("async-task-", CPU_CORES * 5);
        }
        return ExecutorUtils.globalExecutor;
    }

    public static void tryShutdown(AsyncTaskExecutor executor, AsyncTaskExecutor... excludes) {
        if (
                Objects.isNull(executor) || !(executor instanceof ThreadPoolTaskExecutor)
                        || Objects.equals(ExecutorUtils.globalExecutor, executor)
        ) {
            return;
        }
        if (ArrayUtil.isNotEmpty(excludes) && ArrayUtil.contains(excludes, executor)) {
            return;
        }
        try {
            ((ThreadPoolTaskExecutor) executor).shutdown();
        } catch (Exception ignored) {
        }
    }

    public static ThreadPoolTaskExecutor create(String namePrefix, int threadCount) {
        return create(namePrefix, threadCount, Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    }

    public static ThreadPoolTaskExecutor create(
            String namePrefix, int threadCount, int maxPoolSize, int queueSize,
            RejectedExecutionHandler handler
    ) {
        ThreadFactory factory = ThreadFactoryBuilder.create().setNamePrefix(namePrefix).build();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadFactory(factory);
//        executor.setThreadNamePrefix(namePrefix);
        if (queueSize > 0) {
            executor.setQueueCapacity(queueSize);
        }
        RejectedExecutionHandler rejectedHandler = Optional.ofNullable(handler)
                .orElse(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setRejectedExecutionHandler(rejectedHandler);
        executor.initialize();
        return executor;
    }

    public static void executeAsync(Runnable runnable) {
        executeAsync(runnable, "异步任务");
    }

    public static void executeAsync(Runnable runnable, String name) {
        AsyncTaskExecutor executor = getGlobalExecutorOrDefault();
        executeAsync(executor, runnable, name);
    }

    public static <T> Future<T> executeAsync(Callable<T> runnable, String name) {
        return getGlobalExecutorOrDefault().submit(() -> {
            long start = System.currentTimeMillis();
            try {
                return runnable.call();
            } catch (Exception ex) {
                log.error("{} - 异步执行异常", name, ex);
                throw ex;
            } finally {
                log.info("{} - 异步执行耗时：{} ms", name, System.currentTimeMillis() - start);
            }
        });
    }

    public static void executeAsync(AsyncTaskExecutor executor, Runnable runnable, String name) {
        executor.submit(() -> {
            long start = System.currentTimeMillis();
            try {
                runnable.run();
            } catch (Exception ex) {
                if (ex instanceof BusinessException) {
                    log.warn("Business Error：{}", ex.getMessage());
                } else {
                    log.error("{} - 异步执行异常", name, ex);
                }
            } finally {
                log.info("{} - 异步执行耗时：{} ms", name, System.currentTimeMillis() - start);
            }
        });
        tryShutdown(executor);
    }

    /**
     * 分批次异步执行
     *
     * @param dataList 数据列表
     * @param consumer 每批次处理订阅
     * @param <T>      T
     */
    public static <T> void batchExecuteAsync(List<T> dataList, Consumer<List<T>> consumer) {
        batchExecuteAsync(dataList, null, null, consumer, null, null);
    }

    /**
     * 分批次异步执行
     *
     * @param dataList       数据列表
     * @param consumer       每批次处理订阅
     * @param loggerConsumer 日志订阅
     * @param <T>            T
     */
    public static <T> void batchExecuteAsync(
            List<T> dataList, Consumer<List<T>> consumer,
            Consumer<LogMessage> loggerConsumer
    ) {
        batchExecuteAsync(dataList, null, null, consumer, loggerConsumer, null);
    }


    /**
     * 分批次异步执行
     *
     * @param dataList  数据列表
     * @param batchSize 批次数量
     * @param consumer  每批次处理订阅
     * @param <T>       T
     */
    public static <T> void batchExecuteAsync(
            List<T> dataList, Integer batchSize, Consumer<List<T>> consumer
    ) {
        batchExecuteAsync(dataList, null, batchSize, consumer, null, null);
    }

    /**
     * 分批次异步执行
     *
     * @param dataList  数据列表
     * @param batchSize 批次数量
     * @param consumer  每批次处理订阅
     * @param <T>       T
     */
    public static <T> void batchExecuteAsync(
            List<T> dataList, Integer batchSize, Consumer<List<T>> consumer,
            Consumer<LogMessage> loggerConsumer
    ) {
        batchExecuteAsync(dataList, null, batchSize, consumer, loggerConsumer, null);
    }

    /**
     * 分批次异步执行
     *
     * @param dataList       数据列表
     * @param threadCount    线程数
     * @param batchSize      批次数量
     * @param consumer       每批次处理订阅
     * @param loggerConsumer 日志订阅
     * @param <T>            T
     */
    public static <T> void batchExecuteAsync(
            List<T> dataList, Integer threadCount, Integer batchSize, Consumer<List<T>> consumer,
            Consumer<LogMessage> loggerConsumer
    ) {
        batchExecuteAsync(dataList, threadCount, batchSize, consumer, loggerConsumer, null);
    }

    public static void logMsg(LogMessage message) {
        if (Objects.isNull(message) || Objects.isNull(message.getLevel()) || Objects.isNull(message.getMessage())) {
            return;
        }
        Method declaredMethod;
        try {
            declaredMethod = log.getClass().getDeclaredMethod(message.getLevel().name().toLowerCase(), String.class);
            declaredMethod.invoke(log, message.getMessage());
        } catch (Exception ignored) {
        }
    }

    /**
     * 分批次异步执行
     *
     * @param dataList       数据列表
     * @param threadCount    线程数
     * @param batchSize      批次数量
     * @param consumer       每批次处理订阅
     * @param loggerConsumer 日志订阅
     * @param <T>            T
     */
    public static <T> void batchExecuteAsync(
            List<T> dataList, Integer threadCount, Integer batchSize, Consumer<List<T>> consumer,
            Consumer<LogMessage> loggerConsumer, Supplier<Boolean> cancelSupplier
    ) {
        if (CollUtil.isEmpty(dataList) || Objects.isNull(consumer)) {
            return;
        }
        final Supplier<Boolean> cancelCheck = Optional.ofNullable(cancelSupplier)
                .orElse(ExecutorUtils::isInterrupted);
        final Consumer<LogMessage> logger = Optional.ofNullable(loggerConsumer).orElse(ExecutorUtils::logMsg);
        final int corePoolSize = Objects.isNull(threadCount) || threadCount <= 0
                ? 3
                : threadCount;
        ThreadPoolTaskExecutor executorService = create("batch-executor-", corePoolSize);
        final int finalBatchSize = Objects.isNull(batchSize) || batchSize <= 0
                ? 200
                : batchSize;
        final int size = Optional.ofNullable(batchSize).orElse(finalBatchSize);
        final int totalPage = (int) Math.ceil(dataList.size() / (double) size);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int page = 0; page < totalPage; page++) {
            final List<T> currentList = CollUtil.page(page, size, dataList);
            final int currentPage = page;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                if (Objects.equals(true, cancelCheck.get())) {
                    logger.accept(
                            LogMessage.info(String.format("[%d/%d]分批次执行已取消，总 %d 条数据",
                                    currentPage + 1, totalPage, currentList.size()
                            ))
                    );
                    futures.forEach(t -> t.cancel(true));
                    return;
                }
                long start = System.currentTimeMillis();
                logger.accept(
                        LogMessage.debug(String.format("[%d/%d]分批次执行开始，总 %d 条数据",
                                currentPage + 1, totalPage, currentList.size()
                        ))
                );
                try {
                    consumer.accept(currentList);
                } catch (Exception ex) {
                    log.error(String.format("[%d/%d]分批次执行异常", currentPage + 1, totalPage), ex);
                    logger.accept(
                            LogMessage.error(String.format("[%d/%d]分批次执行异常：%s",
                                    currentPage + 1, totalPage, ExceptionUtil.getRootCauseMessage(ex)))
                    );
                } finally {
                    logger.accept(
                            LogMessage.info(String.format("[%d/%d]分批次执行完成，总 %d 条数据, 耗时 %d ms",
                                    currentPage + 1, totalPage, currentList.size(), System.currentTimeMillis() - start))
                    );
                }
            }, executorService);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).join();
        executorService.shutdown();
    }

    public static boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }

    public static <T> T pollingResult(
            Function<Integer, T> func, Integer times
    ) {
        return pollingResult(func, times, null, null, null);
    }

    public static <T> T pollingResult(
            Function<Integer, T> func, Integer times, Long intervalMs
    ) {
        return pollingResult(func, times, intervalMs, null, null);
    }

    public static <T> T pollingResult(
            Function<Integer, T> func, Long intervalMs, Long timeout, TimeUnit unit
    ) {
        return pollingResult(func, Objects::nonNull, null, intervalMs, timeout, unit);
    }

    public static <T> T pollingResult(
            Function<Integer, T> func, Predicate<T> condition, Long intervalMs,
            Long timeout, TimeUnit unit
    ) {
        return pollingResult(func, condition, null, intervalMs, timeout, unit);
    }

    public static <T> T pollingResult(
            Function<Integer, T> func, Integer times, Long intervalMs,
            Long timeout, TimeUnit unit
    ) {
        return pollingResult(func, Objects::nonNull, times, intervalMs, timeout, unit);
    }

    public static <T> T pollingResult(
            Function<Integer, T> func, Predicate<T> condition, Integer times, Long intervalMs,
            Long timeout, TimeUnit unit
    ) {
        unit = Optional.ofNullable(unit).orElse(TimeUnit.SECONDS);
        CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            final long interval =
                    Objects.isNull(intervalMs) || intervalMs <= 0 ? 50 : intervalMs;
            while (true) {
                try {
                    T result = func.apply(counter.get());
                    if (condition.test(result)) {
                        return result;
                    }
                    int count = counter.incrementAndGet();
                    if (Objects.nonNull(times) && times > 0) {
                        if (count >= times) {
                            throw new RuntimeException(String.format("轮询获取异步结果超过最大尝试次数 %d 次", times));
                        }
                    }
                    ThreadUtil.safeSleep(interval);
                } catch (Exception ex) {
                    throw new RuntimeException("轮询获取异步结果异常", ex);
                }
            }
        });
        try {
            if (Objects.nonNull(timeout) && timeout > 0) {
                return completableFuture.get(timeout, unit);
            }
            return completableFuture.get();
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("轮询获取异步结果异常", ex);
        }
    }
}
