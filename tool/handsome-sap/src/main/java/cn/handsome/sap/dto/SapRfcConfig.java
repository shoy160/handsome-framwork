package cn.handsome.sap.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyong
 * @date 2025/4/28
 */
@Getter
@Setter
public class SapRfcConfig {
    private String function;
    private Map<String, Object> params;
    private Boolean errorCheck;

    public SapRfcConfig() {
        this.errorCheck = false;
        this.params = new HashMap<>(0);
    }
}