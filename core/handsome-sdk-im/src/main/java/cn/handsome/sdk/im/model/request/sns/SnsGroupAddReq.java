package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnsGroupAddReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("GroupName")
    private List<String> groupName;
    @JsonProperty("To_Account")
    private List<String> toAccount;
}
