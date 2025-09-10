package cn.handsome.sdk.im.model.request.sns;

import cn.handsome.sdk.im.model.enums.BlackCheckTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class BlackCheckReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private List<String> toAccount;
    @JsonProperty("CheckType")
    private BlackCheckTypeEnum checkType;
}
