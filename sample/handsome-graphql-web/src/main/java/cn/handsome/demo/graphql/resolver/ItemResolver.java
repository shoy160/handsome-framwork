package cn.handsome.demo.graphql.resolver;

import cn.handsome.demo.graphql.model.Item;
import cn.handsome.demo.graphql.model.ItemList;
import cn.handsome.demo.graphql.model.Param;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 * @author shoy
 * @date 2022/1/11
 */
public class ItemResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    public ItemList queryItemList() {
        return new ItemList();
    }

    public Item queryById(Long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("ddd");
        item.setCode("");
        return item;
    }

    public Item updateName(Param param) {
        Item item = new Item();
        item.setId(param.getId());
        item.setName(param.getName());
        item.setCode("");
        return item;
    }
}
