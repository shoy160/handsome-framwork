package cn.handsome.sdk.payment.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Todo
 *
 * @author shay
 * @date 2020/8/7
 */
@Getter
@Setter
@ToString
public class SceneInfoDTO implements Serializable {
    private H5InfoDTO h5_info;
}
