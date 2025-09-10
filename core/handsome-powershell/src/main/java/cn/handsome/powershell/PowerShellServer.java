package cn.handsome.powershell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * todo
 *
 * @author shay
 * @date 2025/3/22
 **/
@Getter
@Setter
@AllArgsConstructor
public class PowerShellServer {
    private volatile boolean healthy = true;
    private String scheme = "http";
    private String host;
    private int port;
    private String serverName = "wsman";
    private String username;
    private String password;
    private Integer connectionTimeout = 5000;
    private Integer socketTimeout = 30_000;

    public PowerShellServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public PowerShellServer(String host, int port, String username, String password) {
        this(host, port);
        this.username = username;
        this.password = password;
    }

    public PowerShellServer(
            String scheme, String host, int port,
            String username, String password
    ) {
        this(host, port, username, password);
        this.scheme = scheme;
    }

    public PowerShellServer(
            String scheme, String host, int port, String name,
            String username, String password
    ) {
        this(scheme, host, port, username, password);
        this.serverName = name;
    }

    public String getEndpoint() {
        return String.format("%s://%s:%d/%s", this.scheme, this.host, this.port, this.serverName);
    }
}
