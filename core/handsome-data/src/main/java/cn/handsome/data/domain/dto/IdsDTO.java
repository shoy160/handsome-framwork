package cn.handsome.data.domain.dto;

import cn.handsome.core.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shay
 * @date 2020/10/13
 */
@Getter
@Setter
@ToString
public class IdsDTO extends BaseDTO {
    private static final long serialVersionUID = -2843453829588127369L;
    private Integer[] ids;
}
