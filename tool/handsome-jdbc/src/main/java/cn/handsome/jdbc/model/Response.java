package cn.handsome.jdbc.model;

/**
 *
 * @author luoyong
 * @date 2025/8/12
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@ToString
public class Response<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return Objects.equals(HttpStatus.OK.value(), this.code);
    }


    public static <T> Response<T> success() {
        Response<T> resp = new Response<>();
        resp.setCode(200);
        resp.setMessage("SUCCESS");
        return resp;
    }

    public static <T> Response<T> success(T obj) {
        Response<T> resp = new Response<>();
        resp.setCode(200);
        resp.setMessage("SUCCESS");
        resp.setData(obj);
        return resp;
    }

    public static <T> Response<T> error(String msg) {
        Response<T> resp = new Response<>();
        resp.setCode(400);
        resp.setMessage(msg);
        return resp;
    }

    public static <T> Response<T> error(T obj) {
        Response<T> resp = new Response<>();
        resp.setCode(400);
        resp.setMessage(obj.toString());
        resp.setData(obj);
        return resp;
    }

    public static <T> Response<T> success(String mes, T data) {
        Response<T> resp = new Response<>();
        resp.setCode(200);
        resp.setMessage(mes);
        resp.setData(data);
        return resp;
    }

    public static <T> Response<T> error(Integer code, T message) {
        Response<T> resp = new Response<>();
        resp.setCode(code);
        resp.setMessage(message.toString());
//        resp.setData(message);
        return resp;
    }

}
