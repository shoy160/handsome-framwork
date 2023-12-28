package cn.handsome.core.compare.model;

import cn.handsome.core.compare.enums.ConditionConjunction;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyong
 * @date 2023/6/26
 */
@Getter
@Setter
public class CompareConditions {
    private ConditionConjunction conjunction;
    private List<CompareCondition> conditions;
    private List<CompareConditions> groups;
    private String groupsId;

    public CompareConditions() {
        this.conditions = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public List<CompareCondition> getConditions() {
        return conditions.stream()
                .filter(t -> StrUtil.isNotBlank(t.getKey()))
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        if (CollUtil.isEmpty(getConditions())) {
            if (CollUtil.isEmpty(groups)) {
                return true;
            }
            return groups.stream().allMatch(CompareConditions::isEmpty);
        }
        return false;
    }
}
