package cn.handsome.core.domain.dto;

import cn.handsome.core.enums.ResultCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 标准结果实体
 *
 * @author shay
 * @date 2020/7/15
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResultDTO<T> implements Serializable {
    private static final long serialVersionUID = -831678996819330527L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 成功标示
     */
    private Boolean success;

    private String message;
    /**
     * 数据
     */
    private T data;
    /**
     * 时间戳
     */
    private Long timestamp;

    private ResultDTO(T data) {
        this.success = true;
        this.code = 200;
        this.data = data;
        this.message = "success";
        this.timestamp = System.currentTimeMillis();
    }

    private ResultDTO(String message, int code) {
        this.success = code == 200;
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    private ResultDTO(String message, int code, T data) {
        this(message, code);
        this.data = data;
    }

    private static <T> ResultDTO<T> result(ResultCode resultCode, String message) {
        return new ResultDTO<>(message, resultCode.getCode());
    }

    private static <T> ResultDTO<T> result(ResultCode resultCode) {
        return new ResultDTO<>(resultCode.getMessage(), resultCode.getCode());
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<>(data);
    }

    public static ResultDTO success() {
        return new ResultDTO(new Object());
    }

    public static <T> ResultDTO<T> successT() {
        return result(ResultCode.SUCCESS);
    }

    public static <T> ResultDTO<T> failT(int code, String message) {
        return new ResultDTO<>(message, code);
    }

    public static <T> ResultDTO<T> failT(ResultCode resultCode) {
        return new ResultDTO<>(resultCode.getMessage(), resultCode.getCode());
    }

    public static <T> ResultDTO<T> failT(ResultCode resultCode, String message) {
        return new ResultDTO<>(message, resultCode.getCode());
    }

    public static ResultDTO fail(int code, String message) {
        return new ResultDTO<>(message, code, new Object());
    }

    public static ResultDTO fail(ResultCode resultCode) {
        return new ResultDTO<>(resultCode.getMessage(), resultCode.getCode(), new Object());
    }

    public static ResultDTO fail(ResultCode resultCode, String message) {
        return new ResultDTO<>(message, resultCode.getCode(), new Object());
    }
}
