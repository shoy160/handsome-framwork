package cn.handsome.powershell.config;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class WinRMConfig {
    /**
     * 本地Exchange服务器主机名或IP
     */
    private String host;
    /**
     * WinRM端口，通常HTTP为5985，HTTPS为5986
     */
    private int port;
    private String domain;
    /**
     * 管理员用户名，通常为DOMAIN\\username格式
     */
    private String username;
    private String password;
    /**
     * 本地环境是否使用HTTPS
     */
    private boolean useHttps;
    /**
     * 会话池配置
     */
    private int maxPoolSize = 10;
    private int minThreadCount = 5;
    private int maxThreadCount = 20;
    /**
     * 会话超时时间
     */
    private int sessionTimeoutSeconds = 300;
    /**
     * 连接超时时间
     */
    private int connectionTimeoutSeconds = 60;

    /**
     * 自定义PowerShell函数
     */
    private List<String> customFunctions = new ArrayList<>();

    /**
     * Exchange版本，如"Exchange2016"
     */
    private String exchangeVersion = "Exchange2016";
    private boolean autoLoadExchangeModule = true;
    
    /**
     * SSL上下文对象，用于自定义SSL证书验证逻辑
     */
    private SSLContext sslContext;
    
    /**
     * 是否信任所有SSL证书，默认为false
     */
    private boolean trustAllCertificates = false;
    
    /**
     * 是否验证主机名，默认为true
     */
    private boolean verifyHostname = true;
    
    /**
     * 命令执行超时时间（秒），默认为300秒
     */
    private int commandTimeoutSeconds = 300;

    // Getters and Setters
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseHttps() {
        return useHttps;
    }

    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinThreadCount() {
        return minThreadCount;
    }

    public void setMinThreadCount(int minThreadCount) {
        this.minThreadCount = minThreadCount;
    }

    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    public void setMaxThreadCount(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public int getSessionTimeoutSeconds() {
        return sessionTimeoutSeconds;
    }

    public void setSessionTimeoutSeconds(int sessionTimeoutSeconds) {
        this.sessionTimeoutSeconds = sessionTimeoutSeconds;
    }

    public int getConnectionTimeoutSeconds() {
        return connectionTimeoutSeconds;
    }

    public void setConnectionTimeoutSeconds(int connectionTimeoutSeconds) {
        this.connectionTimeoutSeconds = connectionTimeoutSeconds;
    }
    
    /**
     * 获取SSL上下文对象
     * @return SSL上下文对象，可能为null
     */
    public SSLContext getSslContext() {
        return sslContext;
    }
    
    /**
     * 设置SSL上下文对象，用于自定义SSL证书验证逻辑
     * @param sslContext SSL上下文对象
     */
    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
    
    /**
     * 获取是否信任所有SSL证书的配置
     * @return true表示信任所有证书，false表示需要验证证书
     */
    public boolean isTrustAllCertificates() {
        return trustAllCertificates;
    }
    
    /**
     * 设置是否信任所有SSL证书
     * @param trustAllCertificates true表示信任所有证书，false表示需要验证证书
     */
    public void setTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }
    
    /**
     * 获取是否验证主机名的配置
     * @return true表示验证主机名，false表示不验证主机名
     */
    public boolean isVerifyHostname() {
        return verifyHostname;
    }
    
    /**
     * 设置是否验证主机名
     * @param verifyHostname true表示验证主机名，false表示不验证主机名
     */
    public void setVerifyHostname(boolean verifyHostname) {
        this.verifyHostname = verifyHostname;
    }
    
    /**
     * 获取命令执行超时时间（秒）
     * @return 超时时间（秒）
     */
    public int getCommandTimeoutSeconds() {
        return commandTimeoutSeconds;
    }
    
    /**
     * 设置命令执行超时时间（秒）
     * @param commandTimeoutSeconds 超时时间（秒）
     */
    public void setCommandTimeoutSeconds(int commandTimeoutSeconds) {
        this.commandTimeoutSeconds = commandTimeoutSeconds;
    }

    public List<String> getCustomFunctions() {
        return customFunctions;
    }

    public void setCustomFunctions(List<String> customFunctions) {
        this.customFunctions = customFunctions;
    }

    public String getExchangeVersion() {
        return exchangeVersion;
    }

    public void setExchangeVersion(String exchangeVersion) {
        this.exchangeVersion = exchangeVersion;
    }

    public boolean isAutoLoadExchangeModule() {
        return autoLoadExchangeModule;
    }

    public void setAutoLoadExchangeModule(boolean autoLoadExchangeModule) {
        this.autoLoadExchangeModule = autoLoadExchangeModule;
    }

    /**
     * 获取WinRM服务URL
     */
    public String getEndpoint() {
        String protocol = useHttps ? "https" : "http";
        return String.format("%s://%s:%d/wsman", protocol, host, port);
    }
}
