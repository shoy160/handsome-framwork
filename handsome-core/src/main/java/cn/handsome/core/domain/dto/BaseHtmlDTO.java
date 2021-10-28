package cn.handsome.core.domain.dto;

import cn.handsome.core.utils.HtmlUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 富文本基础对象
 *
 * @author shoy
 * @date 2021/7/8
 */
@Getter
@Setter
@ToString
public class BaseHtmlDTO extends BaseDTO {
    private String content;

    public String getContent() {
        return HtmlUtil.prettyH5(content);
    }
}
