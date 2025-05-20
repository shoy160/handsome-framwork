package cn.handsome.core.compare.model;

import cn.handsome.core.compare.enums.ConditionOp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author luoyong
 * @date 2023/6/26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompareCondition {
    private String key;
    private String dataType;
    private Object value;
    private ConditionOp op;
}
