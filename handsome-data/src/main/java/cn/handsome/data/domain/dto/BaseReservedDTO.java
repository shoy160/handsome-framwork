package cn.handsome.data.domain.dto;

import cn.handsome.core.domain.HaveReserved;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shay
 * @date 2021/3/5
 */
@Getter
@Setter
@ToString
public class BaseReservedDTO extends BaseDateDTO implements HaveReserved {
    private static final long serialVersionUID = 6203958390162596170L;
    /**
     * 保留字段1
     */
    private String reserved1;

    /**
     * 保留字段2
     */
    private String reserved2;
}
