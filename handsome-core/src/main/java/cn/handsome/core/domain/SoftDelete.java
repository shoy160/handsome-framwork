package cn.handsome.core.domain;

/**
 * 拥有软删除属性
 *
 * @author shay
 * @date 2021/3/5
 */
public interface SoftDelete {
    /**
     * 是否删除
     *
     * @return boolean
     */
    boolean isDel();

    /**
     * 设置软删除
     *
     * @param deleted 是否删除
     */
    void setDel(boolean deleted);
}
