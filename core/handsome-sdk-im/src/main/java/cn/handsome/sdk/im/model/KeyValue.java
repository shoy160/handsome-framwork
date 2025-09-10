package cn.handsome.sdk.im.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class KeyValue {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Value")
    private String value;

    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
