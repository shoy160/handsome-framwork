package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@NoArgsConstructor
@Data
public class FriendGetListReq {

    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private List<String> toAccount;
    @JsonProperty("TagList")
    private List<String> tagList;
}
