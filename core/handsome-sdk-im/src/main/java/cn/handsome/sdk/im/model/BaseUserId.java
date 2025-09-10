package cn.handsome.sdk.im.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseUserId {
    @JsonProperty("UserID")
    private String userId;

    public BaseUserId(String userId) {
        this.userId = userId;
    }
}
