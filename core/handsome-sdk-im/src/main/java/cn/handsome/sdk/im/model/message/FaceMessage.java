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
public class FaceMessage extends BaseMessage {
    @JsonProperty("Index")
    private Integer index;
    @JsonProperty("Data")
    private String data;
}
