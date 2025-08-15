package cn.handsome.script.test;

import cn.handsome.script.IScript;
import cn.handsome.script.impl.GraalvmScript;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyong
 * @date 2025/3/6
 */
//@Slf4j
public class ScriptTest {
    private final IScript script;

    public ScriptTest() {
        this.script = new GraalvmScript();
    }

    @Test
    public void test() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("a", 12);
        variables.put("b", 13);
        for (int i = 0; i < 100; i++) {
            variables.put("c", i);
            Long result = this.script.eval("+new Date()", variables, Long.class);
            System.out.printf("%s: %s%n", result.getClass().getSimpleName(), result);
        }
//        log.info("result: {}", result);
    }
}
