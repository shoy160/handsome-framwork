package cn.handsome.core.gcode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/30
 */
@Getter
@Setter
public class CodeRule {
    /**
     * 编码长度
     */
    private Integer length;
    /**
     * 编码类型
     */
    private CodeType type;
    /**
     * 最少库存量,默认10
     */
    private Integer minStock = 10;
    /**
     * 补仓量，默认200
     */
    private Integer supplyCount = 200;
    /**
     * 格式化,只针对Type为DateFormat
     */
    private String format;

    public CodeRule() {
        this.minStock = 10;
        this.supplyCount = 200;
    }

    public CodeRule(int length, CodeType type) {
        this.length = length;
        this.type = type;
    }
}
