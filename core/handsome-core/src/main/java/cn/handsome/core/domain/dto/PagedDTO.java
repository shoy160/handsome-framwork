package cn.handsome.core.domain.dto;

import cn.handsome.core.lang.Action;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.lang.Func;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据格式
 *
 * @author shay
 * @date 2020/7/15
 */
@Getter
@Setter
@ToString
public class PagedDTO<T> implements Serializable {

    private static final long serialVersionUID = 6511975721611238198L;
    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页显示数量
     */
    private Integer size;
    /**
     * 总数
     */
    private Long total;
    /**
     * 数据列表
     */
    private List<T> list;


    public PagedDTO() {
        this(0L, new ArrayList<>(), 0, 0);
    }

    public PagedDTO(long total, List<T> data) {
        this(total, data, 0, 0);
    }

    public PagedDTO(long total, List<T> data, int page, int size) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.list = data;
    }

    public static <T> PagedDTO<T> paged(List<T> list, long total, int page, int size) {
        return new PagedDTO<>(total, list, page, size);
    }

    public static <T> PagedDTO<T> paged(List<T> list, long total) {
        return paged(list, total, 0, 0);
    }

    public static <T> PagedDTO<T> paged(List<T> list) {
        return paged(list, list.size(), 0, 0);
    }

    public <TD> PagedDTO<TD> convert(Class<TD> clazz) {
        return convert(clazz, null, null);
    }

    public <TD> PagedDTO<TD> convert(Class<TD> clazz, CopyOptions copyOptions) {
        return convert(clazz, copyOptions, null);
    }

    public <TD> PagedDTO<TD> convert(Class<TD> clazz, Action<TD> convertAction) {
        return convert(clazz, null, convertAction);
    }

    public <TD> PagedDTO<TD> convert(Class<TD> clazz, CopyOptions copyOptions, Action<TD> convertAction) {
        List<TD> list = CommonUtils.toListBean(this.list, clazz, copyOptions, convertAction);
        return PagedDTO.paged(list, this.total, this.page, this.size);
    }

    public <TD> PagedDTO<TD> convert(Func<TD, T> convertFunc) {
        List<TD> list = new ArrayList<>();
        for (T record : this.list) {
            TD item = convertFunc.invoke(record);
            if (item == null) {
                continue;
            }
            list.add(item);
        }
        return PagedDTO.paged(list, this.total, this.page, this.size);
    }

    public int getPages() {
        if (this.size == null || this.size <= 0 || this.total == null || this.total <= 0) {
            return 0;
        }
        return Convert.toInt(Math.ceil(this.total / (double) this.size));
    }

    public boolean getNext() {
        if (this.page == null || this.page <= 0 || getPages() <= 0) {
            return false;
        }
        return getPages() > this.page;
    }

    public boolean getPrev() {
        if (this.page == null || this.page <= 0 || getPages() <= 0) {
            return false;
        }
        return this.page > 1;
    }
}
