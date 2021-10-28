package cn.handsome.sdk.im.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/8/6
 */
@Getter
@Setter
public class TextMessage extends BaseMessage {
    @JsonProperty("Text")
    private String text;
}
