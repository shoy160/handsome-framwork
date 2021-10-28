package cn.handsome.demo.web.model.command;

import cn.handsome.core.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/7/28
 */
@Getter
@Setter
public class LoginCmd extends BaseDTO {
    private String name;
}
