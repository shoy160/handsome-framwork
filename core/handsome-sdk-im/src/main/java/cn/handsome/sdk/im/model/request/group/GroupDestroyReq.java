package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解散群组
 *
 * @author shoy
 * @date 2021/6/22
 */
@Data
@NoArgsConstructor
public class GroupDestroyReq {
    @JsonProperty("GroupId")
    private String groupId;
}
