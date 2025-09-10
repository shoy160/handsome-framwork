package cn.handsome.sdk.im.model.response.account;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class AccountQueryResp extends RestResp {
    @JsonProperty("QueryResult")
    private List<AccountQueryItem> result;
    @JsonProperty("ErrorList")
    private List<AccountErrorItem> errors;

    @Getter
    @Setter
    public static class AccountQueryItem {
        @JsonProperty("To_Account")
        private String account;
        @JsonProperty("Status")
        private String status;
        @JsonProperty("Detail")
        private List<PlatformStatus> detail;
    }

    @Getter
    @Setter
    public static class PlatformStatus {
        @JsonProperty("Platform")
        private String platform;
        @JsonProperty("Status")
        private String status;
    }

    @Getter
    @Setter
    public static class AccountErrorItem {
        @JsonProperty("To_Account")
        private String account;
        @JsonProperty("ErrorCode")
        private Integer errorCode;
    }
}
