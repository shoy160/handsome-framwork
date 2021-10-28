package cn.handsome.sdk.im.model.request.openim;

import cn.handsome.sdk.im.model.enums.MsgTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shoy
 * @date 2021/7/8
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgBodyDTO {
    @JsonProperty("MsgType")
    private MsgTypeEnum msgType;

    @JsonProperty("MsgContent")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object msgContent;
}
