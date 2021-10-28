package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 更新好友实体
 *
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class FriendUpdateReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("UpdateItem")
    private List<UpdateItemDTO> updateItem;

    @NoArgsConstructor
    @Data
    public static class UpdateItemDTO {
        @JsonProperty("To_Account")
        private String toAccount;
        @JsonProperty("SnsItem")
        private List<SnsItemDTO> snsItem;

        @NoArgsConstructor
        @Data
        public static class SnsItemDTO {
            @JsonProperty("Tag")
            private String tag;
            @JsonProperty("Value")
            private Object value;
        }
    }
}
