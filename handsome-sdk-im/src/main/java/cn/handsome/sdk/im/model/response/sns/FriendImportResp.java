package cn.handsome.sdk.im.model.response.sns;

import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.ToAccountResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@NoArgsConstructor
@Data
@Getter
@Setter
public class FriendImportResp extends RestResp {

    /**
     * 批量加好友的结果对象数组
     */
    @JsonProperty("ResultItem")
    private List<ToAccountResult> resultItem;
    /**
     * 返回处理失败的用户列表，仅当存在失败用户时才返回该字段
     */
    @JsonProperty("Fail_Account")
    private List<String> failAccount;
}
