package cn.handsome.powershell.model;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 表示一个PowerShell服务器配置
 *
 * @author luoyong
 * @date 2025/9/11
 */
@Getter
@Setter
public class PowerShellServer {
    private String host;
    private int port;
    private String domain;
    private String username;
    private String password;
    /**
     * 使用加密的密码而非明文
     */
    private String encryptedPassword;
    private boolean useHttps = true;
    private String exchangeVersion = "Exchange2016";
    private boolean autoLoadExchangeModule = true;

    /**
     * 服务器权重，用于负载均衡
     */
    private int weight = 1;

    /**
     * 当前服务器的健康状态
     */
    private boolean healthy = true;

    /**
     * 失败计数器
     */
    private int failureCount = 0;

    /**
     * 最大失败次数，超过此值将标记服务器为不健康
     */
    private int maxFailureCount = 3;

    // 性能指标
    private long totalExecutionTime = 0;
    private int totalRequests = 0;
    private int successfulRequests = 0;

    public PowerShellServer(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        setPassword(password);
    }

    public PowerShellServer(String host, int port, String domain, String username, String password) {
        this.host = host;
        this.port = port;
        this.domain = domain;
        this.username = username;
        setPassword(password);
    }

    /**
     * 设置密码（自动加密）
     */
    public void setPassword(String password) {
        this.password = password;
        this.encryptedPassword = encrypt(password);
    }

    /**
     * 获取解密后的密码
     */
    public String getPassword() {
        return encryptedPassword != null && !encryptedPassword.isEmpty()
                ? decrypt(encryptedPassword)
                : password;
    }

    /**
     * 记录一次失败
     */
    public synchronized void recordFailure() {
        this.failureCount++;
        if (this.failureCount >= maxFailureCount) {
            this.healthy = false;
        }
    }

    /**
     * 重置失败计数器，标记服务器为健康
     */
    public synchronized void resetStatus() {
        this.failureCount = 0;
        this.healthy = true;
    }

    /**
     * 记录请求结果，用于性能监控
     */
    public synchronized void recordRequest(boolean success, long executionTime) {
        totalRequests++;
        totalExecutionTime += executionTime;
        if (success) {
            successfulRequests++;
        }
    }

    /**
     * 简单的密码加密（实际项目中应使用更安全的加密方式）
     */
    private String encrypt(String plainText) {
        try {
            // 在实际应用中，密钥应该从安全的配置或密钥管理服务中获取
            // 示例密钥，实际应用中应替换
            String key = "MySecureKey12345";
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt password", e);
        }
    }

    /**
     * 简单的密码解密
     */
    private String decrypt(String encryptedText) {
        try {
            // 在实际应用中，密钥应该从安全的配置或密钥管理服务中获取
            // 示例密钥，实际应用中应替换
            String key = "MySecureKey12345";
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt password", e);
        }
    }
}