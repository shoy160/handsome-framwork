package cn.handsome.sdk.im.model.request.config;

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
public class PortraitSetReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("ProfileItem")
    private List<ProfileItemDTO> profileItem;

    @NoArgsConstructor
    @Data
    public static class ProfileItemDTO {
        @JsonProperty("Tag")
        private String tag;
        @JsonProperty("Value")
        private Object value;
    }
}
