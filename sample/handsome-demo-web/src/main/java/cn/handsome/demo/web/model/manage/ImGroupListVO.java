package cn.handsome.demo.web.model.manage;

import cn.handsome.core.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author shoy
 * @date 2021/8/26
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImGroupListVO extends BaseDTO {
    private Integer total;
    private Long next;
    private List<ImGroupVO> list;

    public ImGroupListVO(Integer total, Long next, List<ImGroupVO> list) {
        this.total = total;
        this.next = next;
        this.list = list;
    }
}
