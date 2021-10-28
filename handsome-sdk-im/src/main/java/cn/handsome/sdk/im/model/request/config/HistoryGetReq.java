package cn.handsome.sdk.im.model.request.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class HistoryGetReq {
    /**
     * 消息类型
     * C2C 表示单发消息 Group 表示群组消息
     */
    @JsonProperty("ChatType")
    private String chatType;
    /**
     * 需要下载的消息记录的时间段
     * 该字段需精确到小时
     * 每次请求只能获取某天某小时的所有单发或群组消息记录
     */
    @JsonProperty("MsgTime")
    private String msgTime;
}
