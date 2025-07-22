package cn.handsome.workflow.domain.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Task Input Parameters
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@Setter
public class InputParameters {
    private String type;
    private Map<String, Object> configuration;
    @JsonProperty("$view")
    private Map<String, Object> view;

    public InputParameters() {
        this.configuration = new HashMap<>(0);
        this.view = new HashMap<>(0);
    }
}
