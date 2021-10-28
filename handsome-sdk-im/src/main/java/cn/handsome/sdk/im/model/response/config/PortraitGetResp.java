package cn.handsome.sdk.im.model.response.config;

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
public class PortraitGetResp extends RestResp {
    @JsonProperty("UserProfileItem")
    private List<UserProfileItemDTO> userProfileItem;
    @JsonProperty("Fail_Account")
    private List<String> failAccount;

    @NoArgsConstructor
    @Data
    public static class UserProfileItemDTO {
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("ProfileItem")
        private List<ProfileItemDTO> profileItem;
        @JsonProperty("ResultCode")
        private Integer resultCode;
        @JsonProperty("ResultInfo")
        private String resultInfo;

        @NoArgsConstructor
        @Data
        public static class ProfileItemDTO {
            @JsonProperty("Tag")
            private String tag;
            @JsonProperty("Value")
            private Object value;
        }
    }
}
