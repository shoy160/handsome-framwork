package cn.handsome.powershell.test;

import cn.handsome.powershell.PowerShellExecutor;
import cn.handsome.powershell.model.PowerShellServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo
 *
 * @author shay
 * @date 2025/3/22
 **/
public class PowerShellTest {
    public static void main(String[] args) throws Exception {
        List<PowerShellServer> servers = Arrays.asList(
                new PowerShellServer("exchange1.example.com", 5986, "admin", "P@ssw0rd1"),
                new PowerShellServer("exchange2.example.com", 5986, "admin", "P@ssw0rd2")
        );

        try (PowerShellExecutor executor = new PowerShellExecutor(servers)) {
            Map<String, Object> params = new HashMap<>();
            params.put("Identity", "user@domain.com");
            params.put("DetailedReport", true);

            String result = executor.invokeFunction("Get-MailboxStatistics", params);
            System.out.println("Execution Result:\n" + result);
        }
    }
}
