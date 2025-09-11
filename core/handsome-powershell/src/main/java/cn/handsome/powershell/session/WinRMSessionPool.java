package cn.handsome.powershell.session;

import cn.handsome.powershell.config.WinRMConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class WinRMSessionPool implements AutoCloseable {
    /**
     * -- GETTER --
     *  获取WinRM配置
     *
     */
    @Getter
    private final WinRMConfig config;

    private final BlockingQueue<WinRMSession> sessionQueue;
    private final ScheduledExecutorService cleaner;
    private final AtomicInteger activeSessionCount;

    /**
     * 构造函数，创建WinRM会话池
     * @param config WinRM配置对象
     */
    public WinRMSessionPool(WinRMConfig config) {
        this.config = config;
        this.sessionQueue = new LinkedBlockingQueue<>(config.getMaxPoolSize());
        this.activeSessionCount = new AtomicInteger(0);

        // 启动定期清理过期会话的线程
        this.cleaner = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "session-cleaner");
            thread.setDaemon(true);
            return thread;
        });

        this.cleaner.scheduleAtFixedRate(
                this::cleanExpiredSessions,
                // 初始延迟60秒
                60,
                // 每30秒检查一次
                30,
                TimeUnit.SECONDS
        );
    }

    /**
     * 从池中获取会话
     */
    public WinRMSession borrowSession() throws Exception {
        // 尝试从队列中获取可用会话
        WinRMSession session = sessionQueue.poll();

        // 如果没有可用会话且未达到最大限制，则创建新会话
        if (session == null && activeSessionCount.get() < config.getMaxPoolSize()) {
            synchronized (this) {
                if (activeSessionCount.get() < config.getMaxPoolSize()) {
                    session = new WinRMSession(config);
                    activeSessionCount.incrementAndGet();
                    return session;
                }
            }
        }

        // 如果没有可用会话且已达到最大限制，则等待
        if (session == null) {
            // 阻塞直到有可用会话
            session = sessionQueue.take();
        }

        // 检查会话是否仍然有效
        if (!session.isConnected() || session.isExpired()) {
            session.close();
            activeSessionCount.decrementAndGet();
            // 递归获取新会话
            return borrowSession();
        }

        return session;
    }

    /**
     * 将会话归还给池
     */
    public void returnSession(WinRMSession session) {
        if (session == null) {
            return;
        }

        // 如果会话已过期或连接关闭，则直接关闭
        if (!session.isConnected() || session.isExpired()) {
            session.close();
            activeSessionCount.decrementAndGet();
            return;
        }

        // 否则将会话放回队列
        if (!sessionQueue.offer(session)) {
            // 如果队列已满，则关闭会话
            session.close();
            activeSessionCount.decrementAndGet();
        }
    }

    /**
     * 清理过期会话
     */
    private void cleanExpiredSessions() {
        List<WinRMSession> toRemove = new ArrayList<>();

        // 检查队列中的会话
        sessionQueue.forEach(session -> {
            if (session.isExpired() || !session.isConnected()) {
                toRemove.add(session);
            }
        });

        // 移除并关闭过期会话
        toRemove.forEach(session -> {
            boolean result = sessionQueue.remove(session);
            if (result) {
                session.close();
                activeSessionCount.decrementAndGet();
            }
        });
    }

    /**
     * 获取当前活跃会话数
     */
    public int getActiveSessionCount() {
        return activeSessionCount.get();
    }

    /**
     * 获取当前空闲会话数
     */
    public int getIdleSessionCount() {
        return sessionQueue.size();
    }

    /**
     * 关闭会话池
     */
    @Override
    public void close() {
        // 关闭清理线程
        cleaner.shutdown();
        try {
            if (!cleaner.awaitTermination(5, TimeUnit.SECONDS)) {
                cleaner.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleaner.shutdownNow();
        }

        // 关闭所有会话
        sessionQueue.forEach(WinRMSession::close);
        sessionQueue.clear();

        activeSessionCount.set(0);
    }
}
