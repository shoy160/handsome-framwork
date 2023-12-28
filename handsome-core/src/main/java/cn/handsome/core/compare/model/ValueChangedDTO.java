package cn.handsome.core.compare.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luoyong
 * @date 2023/12/8
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ValueChangedDTO {
    private Object before;
    private Object after;
}
