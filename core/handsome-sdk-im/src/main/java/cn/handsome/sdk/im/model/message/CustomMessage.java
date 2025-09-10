package cn.handsome.sdk.im.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/8/6
 */
@NoArgsConstructor
@Data
public class CustomMessage extends BaseMessage {
    @JsonProperty("Data")
    private String data;
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("Ext")
    private String ext;
    @JsonProperty("Sound")
    private String sound;
}
