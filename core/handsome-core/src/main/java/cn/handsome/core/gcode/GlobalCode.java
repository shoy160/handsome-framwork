package cn.handsome.core.gcode;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author shoy
 * @date 2021/6/30
 */
public interface GlobalCode {
    /**
     * 注册全局编码
     *
     * @param name   编码规则名称
     * @param length 编码长度
     * @param type   编码类型
     */
    default void register(String name, int length, CodeType type) {
        register(name, new CodeRule(length, type));
    }

    /**
     * 注册全局编码
     *
     * @param name 编码规则名称
     * @param rule 编码规则
     */
    void register(String name, CodeRule rule);

    /**
     * 补仓
     *
     * @param name  编码规则名称
     * @param count 补仓数量
     */
    void fill(String name, int count);

    /**
     * 获取编码
     *
     * @param name 编码规则名称
     * @return 唯一编码
     */
    String code(String name);

    /**
     * 回收编码
     *
     * @param name 编码规则名称
     * @param code 编码
     */
    void recovery(String name, String code);

    /**
     * 已使用编码
     *
     * @param name 编码规则名称
     * @param code 编码
     */
    default void used(String name, String code) {
        used(name, new String[]{code});
    }

    /**
     * 已使用编码
     *
     * @param name  编码规则名称
     * @param codes 编码
     */
    void used(String name, String... codes);

    /**
     * 全局编码信息
     *
     * @param name 编码规则名称
     * @return 编码信息
     */
    CodeInfo info(String name);

}
