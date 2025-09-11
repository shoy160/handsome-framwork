package cn.handsome.powershell.loadbalancer;

import cn.handsome.powershell.config.WinRMConfig;
import cn.handsome.powershell.model.PowerShellServer;
import cn.handsome.powershell.session.WinRMSession;
import cn.handsome.powershell.session.WinRMSessionPool;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PowerShell服务器负载均衡器
 * 用于管理多个Exchange服务器并实现负载均衡
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class PowerShellServerLoadBalancer implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(PowerShellServerLoadBalancer.class);

    @Getter
    private final List<PowerShellServer> servers;

    @Getter
    private final List<WinRMSessionPool> sessionPools;

    private final ScheduledExecutorService healthChecker;

    /**
     * -- SETTER --
     *  设置负载均衡策略
     */
    @Setter
    @Getter
    private LoadBalancingStrategy loadBalancingStrategy = LoadBalancingStrategy.ROUND_ROBIN;

    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);

    /**
     * 构造函数，创建PowerShell服务器负载均衡器
     * @param servers Exchange服务器列表
     */
    public PowerShellServerLoadBalancer(List<PowerShellServer> servers) {
        this.servers = new ArrayList<>(servers);
        this.sessionPools = new ArrayList<>();

        // 为每个服务器创建会话池
        for (PowerShellServer server : servers) {
            sessionPools.add(createSessionPool(server));
        }

        // 启动健康检查线程
        this.healthChecker = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "powershell-health-checker");
            thread.setDaemon(true);
            return thread;
        });

        this.healthChecker.scheduleAtFixedRate(
                this::checkServerHealth,
                60, // 初始延迟60秒
                120, // 每120秒检查一次
                TimeUnit.SECONDS
        );
    }

    /**
     * 根据服务器配置创建会话池
     */
    private WinRMSessionPool createSessionPool(PowerShellServer server) {
        WinRMConfig config = new WinRMConfig();
        config.setHost(server.getHost());
        config.setPort(server.getPort());
        config.setDomain(server.getDomain());
        config.setUsername(server.getUsername());
        config.setPassword(server.getPassword());
        config.setUseHttps(server.isUseHttps());
        config.setExchangeVersion(server.getExchangeVersion());
        config.setAutoLoadExchangeModule(server.isAutoLoadExchangeModule());

        return new WinRMSessionPool(config);
    }

    /**
     * 检查服务器健康状态
     */
    private void checkServerHealth() {
        for (PowerShellServer server : servers) {
            if (!server.isHealthy()) {
                // 尝试重新连接不健康的服务器
                try {
                    WinRMSession testSession = new WinRMSession(createSessionPool(server).getConfig());
                    testSession.executeCommand("Get-Date");
                    testSession.close();
                    server.resetStatus();
                    logger.info("Server {} is back to healthy state", server.getHost());
                } catch (Exception e) {
                    logger.warn("Server {} is still unhealthy: {}", server.getHost(), e.getMessage());
                }
            }
        }
    }

    /**
     * 获取一个可用的服务器会话
     */
    public SessionWithServer borrowSession() throws Exception {
        int maxAttempts = Math.max(3, servers.size());
        Exception lastException = null;

        // 使用循环代替递归，避免栈溢出
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            List<Integer> healthyIndexes = getHealthyServerIndexes();
            if (healthyIndexes.isEmpty()) {
                throw new RuntimeException("No healthy Exchange servers available");
            }

            int selectedIndex = selectServerIndex(healthyIndexes);
            PowerShellServer server = servers.get(selectedIndex);
            WinRMSessionPool pool = sessionPools.get(selectedIndex);

            try {
                WinRMSession session = pool.borrowSession();
                return new SessionWithServer(session, server, selectedIndex);
            } catch (Exception e) {
                // 记录服务器失败
                server.recordFailure();
                logger.warn("Failed to borrow session from server {} (attempt {}): {}",
                        server.getHost(), attempt + 1, e.getMessage());
                lastException = e;

                // 短暂延迟后重试
                Thread.sleep(100);
            }
        }

        // 如果所有尝试都失败，抛出最后一个异常
        throw lastException;
    }

    /**
     * 将会话归还给对应的会话池
     */
    public void returnSession(SessionWithServer sessionWithServer) {
        if (sessionWithServer == null || sessionWithServer.getSession() == null) {
            return;
        }

        int poolIndex = sessionWithServer.getPoolIndex();
        if (poolIndex >= 0 && poolIndex < sessionPools.size()) {
            WinRMSessionPool pool = sessionPools.get(poolIndex);
            pool.returnSession(sessionWithServer.getSession());
        }
    }

    /**
     * 获取健康服务器的索引列表
     */
    private List<Integer> getHealthyServerIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isHealthy()) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    /**
     * 根据负载均衡策略选择服务器索引
     */
    private int selectServerIndex(List<Integer> healthyIndexes) {
        switch (loadBalancingStrategy) {
            case ROUND_ROBIN:
                return roundRobinSelect(healthyIndexes);
            case WEIGHTED_ROUND_ROBIN:
                return weightedRoundRobinSelect(healthyIndexes);
            case LEAST_CONNECTIONS:
                return leastConnectionsSelect(healthyIndexes);
            default:
                return roundRobinSelect(healthyIndexes);
        }
    }

    /**
     * 轮询选择
     */
    private int roundRobinSelect(List<Integer> healthyIndexes) {
        int index = roundRobinCounter.getAndIncrement() % healthyIndexes.size();
        return healthyIndexes.get(Math.abs(index));
    }

    /**
     * 加权轮询选择
     */
    private int weightedRoundRobinSelect(List<Integer> healthyIndexes) {
        List<Integer> weightedIndexes = new ArrayList<>();
        for (Integer index : healthyIndexes) {
            PowerShellServer server = servers.get(index);
            for (int i = 0; i < server.getWeight(); i++) {
                weightedIndexes.add(index);
            }
        }
        int selectIndex = roundRobinCounter.getAndIncrement() % weightedIndexes.size();
        return weightedIndexes.get(Math.abs(selectIndex));
    }

    /**
     * 最少连接数选择
     */
    private int leastConnectionsSelect(List<Integer> healthyIndexes) {
        int selectedIndex = healthyIndexes.get(0);
        int minActiveSessions = Integer.MAX_VALUE;

        for (Integer index : healthyIndexes) {
            int activeSessions = sessionPools.get(index).getActiveSessionCount();
            if (activeSessions < minActiveSessions) {
                minActiveSessions = activeSessions;
                selectedIndex = index;
            }
        }

        return selectedIndex;
    }

    /**
     * 关闭负载均衡器
     */
    @Override
    public void close() {
        // 关闭健康检查线程
        healthChecker.shutdown();
        try {
            if (!healthChecker.awaitTermination(5, TimeUnit.SECONDS)) {
                healthChecker.shutdownNow();
            }
        } catch (InterruptedException e) {
            healthChecker.shutdownNow();
        }

        // 关闭所有会话池
        for (WinRMSessionPool pool : sessionPools) {
            pool.close();
        }
    }

    /**
     * 负载均衡策略枚举
     */
    public enum LoadBalancingStrategy {
        /**
         * 轮询
         */
        ROUND_ROBIN,
        /**
         * 加权轮询
         */
        WEIGHTED_ROUND_ROBIN,
        /**
         * 最少连接数
         */
        LEAST_CONNECTIONS
    }

    /**
     * 包含会话和服务器信息的包装类
     */
    @Getter
    public static class SessionWithServer {
        private final WinRMSession session;
        private final PowerShellServer server;
        private final int poolIndex;

        public SessionWithServer(WinRMSession session, PowerShellServer server, int poolIndex) {
            this.session = session;
            this.server = server;
            this.poolIndex = poolIndex;
        }
    }
}