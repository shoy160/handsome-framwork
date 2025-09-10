package cn.handsome.sdk.im.model.response.sns;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class SnsGroupDeleteResp extends RestResp {
    @JsonProperty("CurrentSequence")
    private Integer currentSequence;
}
