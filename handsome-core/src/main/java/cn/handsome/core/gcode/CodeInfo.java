package cn.handsome.core.gcode;

import cn.handsome.core.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/7/1
 */
@Getter
@Setter
public class CodeInfo extends BaseDTO {
    private String name;
    private CodeRule rule;
    private Long total;
    private Long left;
}
