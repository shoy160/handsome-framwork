package cn.handsome.powershell;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * todo
 *
 * @author shay
 * @date 2025/3/22
 **/
@Slf4j
public class HAConnectionPool implements AutoCloseable {
    private final List<PowerShellServer> servers;
    private final Map<PowerShellServer, WinRMClient> clientPool = new ConcurrentHashMap<>();
    private final PoolingHttpClientConnectionManager connManager;
    private final ScheduledExecutorService healthChecker;

    public HAConnectionPool(List<PowerShellServer> servers) {
        this.servers = new ArrayList<>(servers);
        this.connManager = new PoolingHttpClientConnectionManager();
        this.connManager.setMaxTotal(20);
        this.connManager.setDefaultMaxPerRoute(5);

        this.healthChecker = Executors.newSingleThreadScheduledExecutor();
        initHealthCheck();
        initClients();
    }

    private void initClients() {
        servers.forEach(server ->
                clientPool.put(server, new WinRMClient(server, connManager))
        );
    }

    private void initHealthCheck() {
        healthChecker.scheduleAtFixedRate(() -> {
            servers.parallelStream().forEach(server -> {
                boolean healthy = checkServerHealth(server);
                server.setHealthy(healthy);
            });
        }, 0, 30, TimeUnit.SECONDS);
    }

    private boolean checkServerHealth(PowerShellServer server) {
        try {
            WinRMClient client = clientPool.get(server);
            String result = client.executeCommand("Test-WSMan");
            return result.contains("ProtocolVersion");
        } catch (Exception e) {
            log.warn(String.format("[健康检测]服务 %s 异常：%s", server.getEndpoint(), e.getMessage()), e);
            return false;
        }
    }

    public String executeCommand(String command) throws Exception {
        for (int i = 0; i < servers.size(); i++) {
            PowerShellServer server = getHealthyServer();
            try {
                return clientPool.get(server).executeCommand(command);
            } catch (Exception e) {
                log.warn(String.format("[命令执行]服务 %s 执行异常：%s", server.getEndpoint(), e.getMessage()), e);
//                server.setHealthy(false);
            }
        }
        throw new RuntimeException("All servers unavailable");
    }

    private synchronized PowerShellServer getHealthyServer() {
        return servers.stream()
                .filter(PowerShellServer::isHealthy)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No healthy servers available"));
    }

    @Override
    public void close() throws Exception {
        this.healthChecker.shutdown();
        this.connManager.close();
        for (Map.Entry<PowerShellServer, WinRMClient> entry : this.clientPool.entrySet()) {
            entry.getValue().close();
        }
    }
}
