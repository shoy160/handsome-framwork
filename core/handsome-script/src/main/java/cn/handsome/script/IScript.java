package cn.handsome.script;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2025/3/6
 */
public interface IScript {
    /**
     * 执行脚本
     *
     * @param expression 脚本
     * @param variables  变量
     * @return 执行结果
     */
    Object eval(String expression, Map<String, Object> variables);

    /**
     * 执行脚本
     *
     * @param expression 脚本
     * @param variables  变量
     * @return 执行结果
     */
    default <T> T eval(String expression, Map<String, Object> variables, Class<T> clazz) {
        Object result = eval(expression, variables);
        return clazz.cast(result);
    }

    /**
     * 执行脚本
     *
     * @param expression 脚本
     * @return 执行结果
     */
    default Object eval(String expression) {
        return eval(expression, new HashMap<>(0));
    }

    /**
     * 执行脚本
     *
     * @param expression 脚本
     * @param key        变量名
     * @param item       变量
     * @return 执行结果
     */
    default Object evalItem(String expression, Object item, String key) {
        Map<String, Object> variables = new HashMap<>(1);
        if (Objects.isNull(key) || key.isEmpty()) {
            key = "$item";
        }
        variables.put(key, item);
        return eval(expression, variables);
    }

    /**
     * 执行脚本
     *
     * @param expression 脚本
     * @param item       变量
     * @return 执行结果
     */
    default Object evalItem(String expression, Object item) {
        return evalItem(expression, item, null);
    }
}
