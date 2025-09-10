package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取用户所加入的群组
 *
 * @author shoy
 * @date 2021/6/22
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupJoinedListReq {
    @JsonProperty("Member_Account")
    private String memberAccount;
    @JsonProperty("WithHugeGroups")
    private Integer withHugeGroups;
    @JsonProperty("WithNoActiveGroups")
    private Integer withNoActiveGroups;
    @JsonProperty("ResponseFilter")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseFilterDTO responseFilter;

    @Data
    @NoArgsConstructor
    public static class ResponseFilterDTO {
        @JsonProperty("GroupBaseInfoFilter")
        private List<String> groupBaseInfoFilter;
        @JsonProperty("SelfInfoFilter")
        private List<String> selfInfoFilter;
    }
}
