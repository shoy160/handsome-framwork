package cn.handsome.core.sender;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消息推送任务基类
 *
 * @author luoyong
 * @date 2023/9/19
 */
@Slf4j
public abstract class BaseAsyncSender<T> implements AsyncSender<T> {
    private final static AtomicBoolean CONSUMER_TAG = new AtomicBoolean(false);
    private final LinkedBlockingDeque<T> DETAIL_DEQUE;
    private final int threadCount;
    private final int MAX_WAIT_TIME;
    private final int BATCH_SIZE;
    private final int BATCH_TIME_OUT;

    public BaseAsyncSender() {
        this(null, null, null, null);
    }

    public BaseAsyncSender(int threadCount) {
        this(threadCount, null, null, null);
    }

    public BaseAsyncSender(int threadCount, int batchSize) {
        this(threadCount, null, batchSize, null);
    }

    public BaseAsyncSender(
            Integer threadCount, Integer maxWaitSeconds, Integer batchSize,
            Integer batchTimeoutSecond
    ) {
        this.DETAIL_DEQUE = new LinkedBlockingDeque<>();
        this.threadCount = getIntValue(threadCount, 1);
        this.MAX_WAIT_TIME = getIntValue(maxWaitSeconds, 20) * 1000;
        this.BATCH_SIZE = getIntValue(batchSize, 1);
        this.BATCH_TIME_OUT = getIntValue(batchTimeoutSecond, 5) * 1000;
    }

    @Override
    public void push(T data) {
        if (Objects.nonNull(data)) {
            DETAIL_DEQUE.push(data);
            this.startConsumer();
        }
    }

    @Override
    public void push(Collection<T> dataList) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        dataList.forEach(DETAIL_DEQUE::push);
        this.startConsumer();
    }

    /**
     * 消息推送
     *
     * @param dataList 消息列表
     */
    protected abstract void process(List<T> dataList);

    private void startConsumer() {
        if (!CONSUMER_TAG.compareAndSet(false, true)) {
            return;
        }
        final ExecutorService executorService =
                this.threadCount > 1 ? ThreadUtil.newExecutor(threadCount) : null;
        ThreadUtil.execAsync(() -> {
            ArrayList<T> container = new ArrayList<>();
            long lastConsumerTime = System.currentTimeMillis();
            while (true) {
                try {
                    T data = DETAIL_DEQUE.poll(1, TimeUnit.SECONDS);
                    if (Objects.nonNull(data)) {
                        container.add(data);
                    }
                    // 10 秒无数据，暂时退出消费
                    if (container.isEmpty()) {
                        if (System.currentTimeMillis() - lastConsumerTime > MAX_WAIT_TIME
                                && CONSUMER_TAG.compareAndSet(true, false)
                        ) {
                            break;
                        }
                    } else if (container.size() >= BATCH_SIZE
                            || (System.currentTimeMillis() - lastConsumerTime >= BATCH_TIME_OUT)
                    ) {
                        if (Objects.nonNull(executorService)) {
                            executorService.submit(() -> process(container));
                        } else {
                            process(container);
                        }
                        lastConsumerTime = System.currentTimeMillis();
                        container.clear();
                    }
                    if (Objects.isNull(data)) {
                        ThreadUtil.safeSleep(50);
                    }
                } catch (Exception ex) {
                    log.error("异步推送任务异常", ex);
                    CONSUMER_TAG.compareAndSet(true, false);
                    break;
                }
            }
        });
    }

    private int getIntValue(Integer value, int defaultValue) {
        return (Objects.isNull(value) || value < 0) ? defaultValue : value;
    }
}
