package cn.handsome.demo.graphql.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2022/1/11
 */
@Getter
@Setter
public class ItemList {
    private List<Item> itemList;
    private Integer total;
}
