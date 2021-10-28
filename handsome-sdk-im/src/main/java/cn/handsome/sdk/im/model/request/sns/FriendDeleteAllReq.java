package cn.handsome.sdk.im.model.request.sns;

import cn.handsome.sdk.im.model.enums.FriendDeleteTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/19
 */
@NoArgsConstructor
@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendDeleteAllReq {
    @JsonProperty("From_Account")
    private String fromAccount;

    @JsonProperty("DeleteType")
    private FriendDeleteTypeEnum deleteType;
}
