package cn.handsome.powershell.test;

import cn.handsome.powershell.PowerShellExecutor;
import cn.handsome.powershell.loadbalancer.PowerShellServerLoadBalancer;
import cn.handsome.powershell.model.PowerShellResult;
import cn.handsome.powershell.model.PowerShellServer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多Exchange服务器负载均衡测试类
 * 演示如何使用PowerShellExecutor管理多个Exchange服务器并实现负载均衡
 *
 * @author luoyong
 * @date 2025/9/11
 */
public class PowerShellLoadBalancerTest {
    
    public static void main(String[] args) throws Exception {
        // 配置多个Exchange服务器
        List<PowerShellServer> servers = Arrays.asList(
                // 服务器1
                new PowerShellServer("exchange1.example.com", 5986, "DOMAIN", "admin", "P@ssw0rd1"),
                // 服务器2，设置较高权重
                createServerWithWeight("exchange2.example.com", 5986, "DOMAIN", "admin", "P@ssw0rd2", 2),
                // 服务器3
                new PowerShellServer("exchange3.example.com", 5986, "DOMAIN", "admin", "P@ssw0rd3")
        );
        
        // 创建PowerShell执行器，支持多服务器负载均衡
        try (PowerShellExecutor executor = new PowerShellExecutor(servers)) {
            // 设置负载均衡策略（可选，默认为轮询）
            // executor.setLoadBalancingStrategy(PowerShellServerLoadBalancer.LoadBalancingStrategy.WEIGHTED_ROUND_ROBIN);
            // executor.setLoadBalancingStrategy(PowerShellServerLoadBalancer.LoadBalancingStrategy.LEAST_CONNECTIONS);
            
            // 示例1：执行简单命令
            System.out.println("=== 执行简单命令 ===");
            PowerShellResult result1 = executor.executeCommand("Get-Mailbox -ResultSize 5 | Select-Object DisplayName,PrimarySmtpAddress");
            System.out.println("命令执行结果：" + !result1.isHasError());
            System.out.println("输出内容：\n" + result1.getOutput());
            
            // 示例2：执行脚本
            System.out.println("\n=== 执行脚本 ===");
            String script = "# 这是一个示例脚本\n"
                    + "$mailboxes = Get-Mailbox -ResultSize 10\n"
                    + "$mailboxes | ForEach-Object {\n"
                    + "    [PSCustomObject]@{\n"
                    + "        DisplayName = $_.DisplayName\n"
                    + "        Email = $_.PrimarySmtpAddress\n"
                    + "        RecipientType = $_.RecipientTypeDetails\n"
                    + "    }\n"
                    + "}";
            PowerShellResult result2 = executor.executeCommand(script);
            System.out.println("脚本执行结果：" + !result2.isHasError());
            System.out.println("脚本输出内容：\n" + result2.getOutput());
            
            // 示例3：调用函数（带参数）
            System.out.println("\n=== 调用函数（带参数）===");
            Map<String, Object> params = new HashMap<>();
            params.put("Identity", "user@domain.com");
            params.put("DetailedReport", true);
            String result3 = executor.invokeFunction("Get-MailboxStatistics", params);
            System.out.println("函数调用结果：\n" + result3);
            
            // 示例4：并发执行多个命令
            System.out.println("\n=== 并发执行多个命令 ===");
            List<String> commands = Arrays.asList(
                    "Get-Mailbox -ResultSize 3 | Select-Object DisplayName",
                    "Get-TransportService | Select-Object Name,Identity",
                    "Get-Database | Select-Object Name,ServerName,Size"
            );
            List<PowerShellResult> results = executor.executeCommandsConcurrently(commands);
            for (int i = 0; i < results.size(); i++) {
                PowerShellResult r = results.get(i);
                System.out.println("命令 " + (i+1) + " 执行结果：" + !r.isHasError());
                System.out.println("输出内容：\n" + r.getOutput());
                System.out.println();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建指定权重的服务器配置
     */
    private static PowerShellServer createServerWithWeight(String host, int port, String domain, String username, String password, int weight) {
        PowerShellServer server = new PowerShellServer(host, port, domain, username, password);
        server.setWeight(weight);
        return server;
    }
}