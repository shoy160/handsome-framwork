package cn.handsome.sdk.im.model.request.sns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class FriendImportReq {
    /**
     * 需要为该 UserID 添加好友 (必填)
     */
    @JsonProperty("From_Account")
    private String fromAccount;
    /**
     * 好友结构体对象
     */
    @JsonProperty("AddFriendItem")
    private List<AddFriendItemDTO> addFriendItem;

    @Getter
    @Setter
    @ToString
    public static class AddFriendItemDTO {
        /**
         * 好友的 UserID
         */
        @JsonProperty("To_Account")
        private String toAccount;
        /**
         * From_Account 对 To_Account 的好友备注
         */
        @JsonProperty("Remark")
        private String remark;
        /**
         * From_Account 对 To_Account 的好友备注时间
         */
        @JsonProperty("RemarkTime")
        private Integer remarkTime;

        /**
         * From_Account 对 To_Account 的分组信息
         */
        @JsonProperty("GroupName")
        private List<String> groupName;
        /**
         * 加好友来源字段
         */
        @JsonProperty("AddSource")
        private String addSource;

        /**
         * From_Account 和 To_Account 形成好友关系时的附言信息
         */
        @JsonProperty("AddWording")
        private String addWording;
        /**
         * From_Account 和 To_Account 形成好友关系的时间
         */
        @JsonProperty("AddTime")
        private Integer addTime;

        /**
         * From_Account 对 To_Account 的自定义好友数据
         */
        @JsonProperty("CustomItem")
        private List<CustomItemDTO> customItem;

        @Getter
        @Setter
        @ToString
        public static class CustomItemDTO {
            /**
             * 自定义好友字段的名称
             */
            @JsonProperty("Tag")
            private String tag;
            /**
             * 自定义好友字段的值
             */
            @JsonProperty("Value")
            private Object value;
        }
    }
}
