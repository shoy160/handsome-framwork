package cn.handsome.core.compare;


import cn.handsome.core.compare.enums.ConditionOp;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public interface ICompare {
    /**
     * 获取条件操作
     *
     * @return 条件操作
     */
    ConditionOp getOperation();

    /**
     * 数据对比操作
     *
     * @param value     值
     * @param compareTo 对比值
     * @return boolean
     */
    boolean compare(Object value, Object compareTo);
}
