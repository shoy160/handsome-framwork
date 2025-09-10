package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendGetReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("StartIndex")
    private int startIndex;
    @JsonProperty("StandardSequence")
    private int standardSequence;
    @JsonProperty("CustomSequence")
    private int customSequence;
}
