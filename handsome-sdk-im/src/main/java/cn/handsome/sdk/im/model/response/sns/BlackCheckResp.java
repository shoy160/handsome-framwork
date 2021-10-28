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
public class BlackCheckResp extends RestResp {
    @JsonProperty("BlackListCheckItem")
    private List<BlackListCheckItemDTO> blackListCheckItem;
    @JsonProperty("Fail_Account")
    private List<String> failAccount;

    @NoArgsConstructor
    @Data
    public static class BlackListCheckItemDTO {
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("Relation")
        private String relation;
        @JsonProperty("ResultCode")
        private Integer resultCode;
        @JsonProperty("ResultInfo")
        private String resultInfo;
    }
}
