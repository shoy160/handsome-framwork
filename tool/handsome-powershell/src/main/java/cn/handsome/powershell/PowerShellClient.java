package cn.handsome.powershell;

import cn.handsome.powershell.config.WinRMConfig;
import cn.handsome.powershell.model.PowerShellResult;
import cn.handsome.powershell.session.WinRMSession;
import cn.handsome.powershell.session.WinRMSessionPool;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class PowerShellClient implements AutoCloseable {
    private final WinRMSessionPool sessionPool;
    private final ExecutorService executorService;

    public PowerShellClient(WinRMConfig config) {
        this.sessionPool = new WinRMSessionPool(config);
        // 创建与最大会话数匹配的线程池
        // 初始化线程池，用于并发执行命令
        this.executorService = new ThreadPoolExecutor(
                config.getMinThreadCount(),
                config.getMaxThreadCount(),
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);

                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r, "powershell-client-" + threadNumber.getAndIncrement());
                        thread.setDaemon(true);
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 执行单个PowerShell命令
     */
    public PowerShellResult executeCommand(String command) throws Exception {
        WinRMSession session = null;
        try {
            session = sessionPool.borrowSession();
            return session.executeCommand(command);
        } finally {
            if (session != null) {
                sessionPool.returnSession(session);
            }
        }
    }

    /**
     * 执行PowerShell脚本
     */
    public PowerShellResult executeScript(String scriptContent) throws Exception {
        WinRMSession session = null;
        try {
            session = sessionPool.borrowSession();
            return session.executeScript(scriptContent);
        } finally {
            if (session != null) {
                sessionPool.returnSession(session);
            }
        }
    }

    /**
     * 并发执行多个命令
     */
    public List<PowerShellResult> executeCommandsConcurrently(List<String> commands) throws Exception {
        List<Callable<PowerShellResult>> tasks = commands.stream()
                .map(command -> (Callable<PowerShellResult>) () -> executeCommand(command))
                .collect(Collectors.toList());

        List<Future<PowerShellResult>> futures = executorService.invokeAll(tasks);

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new RuntimeException("Command execution failed", e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取会话池状态信息
     */
    public String getPoolStatus() {
        return String.format("Active sessions: %d, Idle sessions: %d",
                sessionPool.getActiveSessionCount(),
                sessionPool.getIdleSessionCount());
    }

    /**
     * 关闭客户端
     */
    @Override
    public void close() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        sessionPool.close();
    }
}
