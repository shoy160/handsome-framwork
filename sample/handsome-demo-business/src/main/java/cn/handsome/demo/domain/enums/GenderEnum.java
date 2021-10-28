package cn.handsome.demo.domain.enums;

import cn.handsome.core.enums.BaseNamedEnum;

/**
 * 性别枚举
 *
 * @author shoy
 * @date 2021/5/28
 */
public enum GenderEnum implements BaseNamedEnum {
    /**
     * 保密
     */
    Secret(3, "保密"),
    /**
     * 男士
     */
    Male(1, "男士"),
    /**
     * 女士
     */
    Female(2, "女士");

    private final Integer value;
    private final String name;

    GenderEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
