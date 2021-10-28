package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class BlackGetReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("StartIndex")
    private Integer startIndex;
    @JsonProperty("MaxLimited")
    private Integer maxLimited;
    @JsonProperty("LastSequence")
    private Integer lastSequence;
}
