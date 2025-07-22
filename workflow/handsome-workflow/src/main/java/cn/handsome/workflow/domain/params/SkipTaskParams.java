package cn.handsome.workflow.domain.params;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
@Getter
@Setter
public class SkipTaskParams {
    private Map<String, Object> taskInput;
    private Map<String, Object> taskOutput;
    private Object taskInputMessage;
    private Object taskOutputMessage;

    public SkipTaskParams() {
        this.taskInput = new HashMap<>(0);
        this.taskOutput = new HashMap<>(0);
    }
}
