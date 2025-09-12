package cn.handsome.powershell;

import cn.handsome.powershell.loadbalancer.PowerShellServerLoadBalancer;
import cn.handsome.powershell.model.PowerShellResult;
import cn.handsome.powershell.model.PowerShellServer;
import cn.handsome.powershell.session.WinRMSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * PowerShell执行器，支持在多个Exchange服务器之间进行负载均衡的命令执行
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class PowerShellExecutor implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(PowerShellExecutor.class);

    private final PowerShellServerLoadBalancer loadBalancer;
    private final ExecutorService executorService;
    private final int maxRetryAttempts = 3;
    /**
     * 操作超时时间（秒），默认5分钟
     */
    private final long operationTimeout = 300;

    /**
     * 构造函数，创建PowerShell执行器
     * @param servers Exchange服务器列表
     */
    public PowerShellExecutor(List<PowerShellServer> servers) {
        this.loadBalancer = new PowerShellServerLoadBalancer(servers);
        // 创建与服务器数量相匹配的线程池
        int corePoolSize = Math.max(servers.size(), 5);
        this.executorService = new ThreadPoolExecutor(
                // 核心线程数
                corePoolSize,
                // 最大线程数
                corePoolSize,
                // 空闲线程存活时间
                60L, TimeUnit.SECONDS,
                // 阻塞队列
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r, "powershell-executor-");
                    thread.setDaemon(true);
                    return thread;
                },
                // 拒绝策略，需要使用new创建实例
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 执行单个PowerShell命令
     */
    public PowerShellResult executeCommand(String command) throws Exception {
        return executeCommandWithRetry(command, 0);
    }

    /**
     * 执行PowerShell脚本
     */
    public PowerShellResult executeScript(String scriptContent) throws Exception {
        return executeScriptWithRetry(scriptContent, 0);
    }

    /**
     * 调用PowerShell函数
     */
    public String invokeFunction(String functionName, Map<String, Object> parameters) throws Exception {
        String command = buildFunctionCommand(functionName, parameters);
        PowerShellResult result = executeCommand(command);
        if (!result.isHasError()) {
            return result.getOutput();
        } else {
            throw new RuntimeException("Function execution failed");
        }
    }

    /**
     * 并发执行多个命令
     */
    public List<PowerShellResult> executeCommandsConcurrently(List<String> commands) throws Exception {
        List<Callable<PowerShellResult>> tasks = commands.stream()
                .map(command -> (Callable<PowerShellResult>) () -> executeCommand(command))
                .collect(Collectors.toList());

        List<Future<PowerShellResult>> futures = executorService.invokeAll(
                tasks,
                operationTimeout,
                TimeUnit.SECONDS
        );

        List<PowerShellResult> results = new ArrayList<>(commands.size());
        for (Future<PowerShellResult> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                PowerShellResult errorResult = new PowerShellResult();
                errorResult.setHasError(true);
                errorResult.setOutput(e.getMessage());
                results.add(errorResult);
            }
        }

        return results;
    }

    /**
     * 构建函数调用命令
     */
    private String buildFunctionCommand(String functionName, Map<String, Object> parameters) {
        StringBuilder commandBuilder = new StringBuilder(functionName);

        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();

                commandBuilder.append(" -").append(paramName);

                if (paramValue != null) {
                    String paramValueStr;
                    if (paramValue instanceof Boolean) {
                        paramValueStr = paramValue.toString();
                    } else if (paramValue instanceof String) {
                        // 字符串值需要加引号
                        paramValueStr = String.format("\"%s\"", escapeQuotes((String) paramValue));
                    } else {
                        paramValueStr = paramValue.toString();
                    }
                    commandBuilder.append(" ").append(paramValueStr);
                }
            }
        }

        return commandBuilder.toString();
    }

    /**
     * 转义字符串中的引号
     */
    private String escapeQuotes(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\"", "\\\"").replace("'", "\\'");
    }

    /**
     * 带重试机制的命令执行
     */
    private PowerShellResult executeCommandWithRetry(String command, int attempt) throws Exception {
        PowerShellServerLoadBalancer.SessionWithServer sessionWithServer = null;
        try {
            sessionWithServer = loadBalancer.borrowSession();
            WinRMSession session = sessionWithServer.getSession();
            PowerShellServer server = sessionWithServer.getServer();

            logger.debug("Executing command on server {}: {}", server.getHost(), command);
            PowerShellResult result = session.executeCommand(command);

            if (result.isHasError() && attempt < maxRetryAttempts) {
                logger.warn("Command execution failed on server {}, retrying ({}/{}): {}",
                        server.getHost(), attempt + 1, maxRetryAttempts, result.getOutput());
                // 标记当前服务器可能有问题
                server.recordFailure();
                // 尝试在其他服务器上重试
                return executeCommandWithRetry(command, attempt + 1);
            }

            return result;
        } catch (Exception e) {
            // 确保错误结果中包含命令信息
            String errorMessage = String.format("Command execution failed: %s\nError: %s",
                    command, e.getMessage());
            if (attempt < maxRetryAttempts) {
                logger.warn("Exception executing command, retrying ({}/{}): {}",
                        attempt + 1, maxRetryAttempts, errorMessage);
                return executeCommandWithRetry(command, attempt + 1);
            }
            logger.error("Failed to execute PowerShell command: {}", command, e);
            throw new Exception(errorMessage, e);
        } finally {
            if (sessionWithServer != null) {
                loadBalancer.returnSession(sessionWithServer);
            }
        }
    }

    /**
     * 带重试机制的脚本执行
     */
    private PowerShellResult executeScriptWithRetry(String scriptContent, int attempt) throws Exception {
        PowerShellServerLoadBalancer.SessionWithServer sessionWithServer = null;
        try {
            sessionWithServer = loadBalancer.borrowSession();
            WinRMSession session = sessionWithServer.getSession();

            logger.debug("Executing script on server: {}", sessionWithServer.getServer().getHost());
            PowerShellResult result = session.executeScript(scriptContent);

            if (result.isHasError() && attempt < maxRetryAttempts) {
                logger.warn("Script execution failed, retrying ({}/{}): {}",
                        attempt + 1, maxRetryAttempts, result.getOutput());
                // 标记当前服务器可能有问题
                sessionWithServer.getServer().recordFailure();
                // 尝试在其他服务器上重试
                return executeScriptWithRetry(scriptContent, attempt + 1);
            }

            return result;
        } catch (Exception e) {
            // 确保错误结果中包含脚本信息
            // 为了避免输出过长，只包含脚本前100个字符
            String scriptPreview = scriptContent.length() > 100 ? scriptContent.substring(0, 100) + "..." : scriptContent;
            String errorMessage = String.format("Script execution failed: %s\nError: %s",
                    scriptPreview, e.getMessage());
            if (attempt < maxRetryAttempts) {
                logger.warn("Exception executing script, retrying ({}/{}): {}",
                        attempt + 1, maxRetryAttempts, errorMessage);
                return executeScriptWithRetry(scriptContent, attempt + 1);
            }
            logger.error("Failed to execute PowerShell script", e);
            throw new Exception(errorMessage, e);
        } finally {
            if (sessionWithServer != null) {
                loadBalancer.returnSession(sessionWithServer);
            }
        }
    }

    /**
     * 设置负载均衡策略
     * @param strategy 负载均衡策略
     */
    public void setLoadBalancingStrategy(PowerShellServerLoadBalancer.LoadBalancingStrategy strategy) {
        this.loadBalancer.setLoadBalancingStrategy(strategy);
    }

    /**
     * 关闭执行器
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
        loadBalancer.close();
    }
}