package cn.handsome.sdk.im.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class ResultResp {
    @JsonProperty("ResultCode")
    private Integer code;
    @JsonProperty("ResultInfo")
    private String info;
}
