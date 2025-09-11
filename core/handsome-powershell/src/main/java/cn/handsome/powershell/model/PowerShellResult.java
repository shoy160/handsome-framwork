package cn.handsome.powershell.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author luoyong
 * @date 2025/9/11
 */
@Getter
@Setter
public class PowerShellResult {
    private String command;
    private String output;
    private boolean hasError;
    /**
     * 执行时间，毫秒
     */
    private long executionTime;

    public PowerShellResult() {
    }

    public PowerShellResult(String command, String output, boolean hasError, long executionTime) {
        this.command = command;
        this.output = output;
        this.hasError = hasError;
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "Command: " + command + "\n" +
                "Has Error: " + hasError + "\n" +
                "Execution Time: " + executionTime + "ms\n" +
                "Output:\n" + output;
    }
}
