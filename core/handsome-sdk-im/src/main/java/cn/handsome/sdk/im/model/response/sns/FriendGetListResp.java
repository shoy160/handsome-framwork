package cn.handsome.sdk.im.model.response.sns;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class FriendGetListResp extends RestResp {

    @JsonProperty("InfoItem")
    private List<InfoItemDTO> infoItem;

    @NoArgsConstructor
    @Data
    public static class InfoItemDTO {
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("SnsProfileItem")
        private List<SnsProfileItemDTO> snsProfileItem;
        @JsonProperty("ResultCode")
        private Integer resultCode;
        @JsonProperty("ResultInfo")
        private String resultInfo;

        @NoArgsConstructor
        @Data
        public static class SnsProfileItemDTO {
            @JsonProperty("Tag")
            private String tag;
            @JsonProperty("Value")
            private Object value;
        }
    }
}
