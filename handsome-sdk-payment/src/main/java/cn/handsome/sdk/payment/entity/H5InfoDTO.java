package cn.handsome.sdk.payment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Todo
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@ToString
public class H5InfoDTO {
    private String type;
    private String wap_name;
    private String wap_url;

    public H5InfoDTO() {
        this.type = "Wap";
    }
}
