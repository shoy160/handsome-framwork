package cn.handsome.core.domain;

/**
 * 创建者
 *
 * @author shoy
 * @date 2021/12/21
 */
public interface HaveCreator<T> {
    /**
     * 获取创建者ID
     *
     * @return id
     */
    T getCreatorId();

    /**
     * 设置创建者ID
     *
     * @param id id
     */
    void setCreatorId(T id);
}
