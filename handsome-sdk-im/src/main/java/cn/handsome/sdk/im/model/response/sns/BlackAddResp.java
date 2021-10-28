package cn.handsome.sdk.im.model.response.sns;

import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.ToAccountResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 添加黑名单响应实体
 *
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
@NoArgsConstructor
public class BlackAddResp extends RestResp {
    @JsonProperty("ResultItem")
    private List<ToAccountResult> resultItem;
    @JsonProperty("Fail_Account")
    private List<String> failAccount;
}
