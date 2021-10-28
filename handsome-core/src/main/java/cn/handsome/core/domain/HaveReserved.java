package cn.handsome.core.domain;

/**
 * 拥有扩展属性
 *
 * @author shay
 * @date 2021/3/5
 */
public interface HaveReserved {
    /**
     * 获取扩展属性1
     *
     * @return String
     */
    String getReserved1();

    /**
     * 获取扩展属性2
     *
     * @return String
     */
    String getReserved2();

    /**
     * 设置扩展属性1
     *
     * @param reserved1 扩展属性1
     */
    void setReserved1(String reserved1);

    /**
     * 设置扩展属性2
     *
     * @param reserved2 扩展属性2
     */
    void setReserved2(String reserved2);
}
