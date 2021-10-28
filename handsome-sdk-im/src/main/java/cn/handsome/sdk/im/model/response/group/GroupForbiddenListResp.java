package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupForbiddenListResp extends RestResp {

    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("ShuttedUinList")
    private List<ShuttedUinListDTO> shuttedUinList;

    @NoArgsConstructor
    @Data
    public static class ShuttedUinListDTO {
        @JsonProperty("Member_Account")
        private String memberAccount;
        @JsonProperty("ShuttedUntil")
        private Integer shuttedUntil;
    }
}
