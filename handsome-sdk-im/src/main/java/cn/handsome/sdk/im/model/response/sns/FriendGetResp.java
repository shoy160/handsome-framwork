package cn.handsome.sdk.im.model.response.sns;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
public class FriendGetResp extends RestResp {
    /**
     * 好友总数
     */
    @JsonProperty("FriendNum")
    private Integer friendNum;

    /**
     * 好友列表
     */
    @JsonProperty("UserDataItem")
    private List<FriendItem> userDataItem;

    /**
     * 标配好友数据的 Sequence
     */
    @JsonProperty("StandardSequence")
    private Integer standardSequence;

    /**
     * 自定义好友数据的 Sequence
     */
    @JsonProperty("CustomSequence")
    private Integer customSequence;

    /**
     * 分页的结束标识，非0值表示已完成全量拉取
     */
    @JsonProperty("CompleteFlag")
    private Integer completeFlag;

    /**
     * 分页接口下一页的起始位置
     */
    @JsonProperty("NextStartIndex")
    private Integer nextStartIndex;

    @Getter
    @Setter
    public static class FriendItem {
        /**
         * 好友的 UserID
         */
        @JsonProperty("To_Account")
        private String toAccount;
        /**
         * 保存好友数据的数组
         * 数组每一个元素都包含一个 Tag 字段和一个 Value 字段
         */
        @JsonProperty("ValueItem")
        private List<FriendTagItem> valueItem;
    }

    @Getter
    @Setter
    public static class FriendTagItem {
        /**
         * 好友字段的名称
         */
        @JsonProperty("Tag")
        private String tag;

        /**
         * 好友字段的值
         */
        @JsonProperty("Value")
        private Object value;
    }
}
