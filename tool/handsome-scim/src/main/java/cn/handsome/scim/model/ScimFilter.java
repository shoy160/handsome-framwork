package cn.handsome.scim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScimFilter {
    /**
     * "and", "or", "eq", "ne", "co", "sw", "ew", "gt", "lt", "ge", "le"
     */
    private String operator;
    private ScimFilter left;
    private ScimFilter right;
    private String attribute;
    private String value;
}
