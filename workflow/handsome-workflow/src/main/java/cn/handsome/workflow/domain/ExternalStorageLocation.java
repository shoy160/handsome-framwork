package cn.handsome.workflow.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
@Getter
@Setter
@ToString
public class ExternalStorageLocation {
    private String uri;
    private String path;
}
