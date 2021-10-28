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
public class BlackGetResp extends RestResp {
    @JsonProperty("BlackListItem")
    private List<BlackListItemDTO> blackListItem;
    @JsonProperty("StartIndex")
    private Integer startIndex;
    @JsonProperty("CurruentSequence")
    private Integer currentSequence;

    @NoArgsConstructor
    @Data
    public static class BlackListItemDTO {
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("AddBlackTimeStamp")
        private Integer addBlackTimeStamp;
    }
}
