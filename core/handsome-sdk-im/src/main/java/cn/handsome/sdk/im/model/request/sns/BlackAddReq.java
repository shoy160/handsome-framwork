package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加黑名单请求实体
 *
 * @author shoy
 * @date 2021/6/19
 */
@NoArgsConstructor
@Data
public class BlackAddReq {

    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private List<String> toAccount;
}
