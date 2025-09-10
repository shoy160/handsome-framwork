package cn.handsome.powershell;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * todo
 *
 * @author shay
 * @date 2025/3/22
 **/
public class PowerShellExecutor implements AutoCloseable {
    private final HAConnectionPool connectionPool;

    public PowerShellExecutor(List<PowerShellServer> servers) {
        this.connectionPool = new HAConnectionPool(servers);
    }

    public String invoke(String command) throws Exception {
        return connectionPool.executeCommand(command);
    }

    public String invokeFunction(String functionName, Map<String, Object> parameters) throws Exception {
        String script = buildScript(functionName, parameters);
        return connectionPool.executeCommand(script);
    }

    private String buildScript(String functionName, Map<String, Object> params) {
        String paramString = params.entrySet().stream()
                .map(e -> String.format("-%s %s", e.getKey(), formatParam(e.getValue())))
                .collect(Collectors.joining(" "));
        return String.format("%s %s", functionName, paramString);
    }

    private Object formatParam(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        } else if (value instanceof Boolean) {
            return "$" + value.toString().toLowerCase();
        }
        return value;
    }

    @Override
    public void close() throws Exception {
        this.connectionPool.close();
    }
}
