package cn.handsome.sdk.im.model.response.account;

import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.ResultResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class AccountCheckResp extends RestResp {
    @JsonProperty("ResultItem")
    private List<CheckItem> results;

    @Getter
    @Setter
    public static class CheckItem extends ResultResp {
        @JsonProperty("AccountStatus")
        private String status;
    }
}
