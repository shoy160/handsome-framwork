package cn.handsome.sdk.im.model.response.sns;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class SnsGroupGetResp extends RestResp {
    @JsonProperty("ResultItem")
    private List<ResultItemDTO> resultItem;
    @JsonProperty("CurrentSequence")
    private Integer currentSequence;

    @NoArgsConstructor
    @Data
    public static class ResultItemDTO {
        @JsonProperty("GroupName")
        private String groupName;
        @JsonProperty("FriendNumber")
        private Integer friendNumber;
        @JsonProperty("To_Account")
        private List<String> toAccount;
    }
}
