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
public class SoundMessage extends BaseMessage {
    @JsonProperty("Url")
    private String url;
    @JsonProperty("Size")
    private Integer size;
    @JsonProperty("Second")
    private Integer second;
    @JsonProperty("Download_Flag")
    private Integer downloadFlag;
}
