package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
public class GroupImportResp extends RestResp {
    @JsonProperty("GroupId")
    private String groupId;
}
