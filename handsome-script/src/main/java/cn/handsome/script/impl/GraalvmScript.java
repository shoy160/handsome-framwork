package cn.handsome.script.impl;

import cn.handsome.script.BaseScript;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.util.Map;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2025/3/6
 */
public class GraalvmScript extends BaseScript implements AutoCloseable {
    private Context context;

    public GraalvmScript() {
        super("graal.js");
    }

    private Context getContext() {
        if (Objects.nonNull(context)) {
            return this.context;
        }
        return this.context = createContext();
    }

    @Override
    public Object eval(String expression, Map<String, Object> variables) {
        return eval(expression, variables, Object.class);
    }

    @Override
    public <T> T eval(String expression, Map<String, Object> variables, Class<T> clazz) {
        Context context = getContext();
        // 获取全局对象
        Value global = context.getBindings("js");
        if (Objects.nonNull(variables) && !variables.isEmpty()) {
            // 定义一个安全的函数供 JavaScript 调用
            variables.forEach(global::putMember);
        }
        // 要执行的 JavaScript 代码
        Source source = Source.create("js", resolveScript(expression));
        Value result = context.eval(source);
        if (Objects.nonNull(variables) && !variables.isEmpty()) {
            // 运行完成，清理全局变量
            variables.keySet().forEach(global::removeMember);
        }
        return result.as(clazz);
    }

    private Context createContext() {
        return Context.newBuilder("js")
                // 禁止访问主机环境
                .allowHostAccess(HostAccess.NONE)
                // 禁止输入输出操作
                .allowIO(false)
                // 禁止创建新进程
                .allowCreateProcess(false)
                // 禁止本地访问
                .allowNativeAccess(false)
                .option("js.ecmascript-version", "2020")
                // 禁用解释器模式警告
                .option("engine.WarnInterpreterOnly", "false")
//                .limitExecutionTime(Duration.ofSeconds(1))
                .build();
    }

    @Override
    public void close() {
        if (Objects.nonNull(context)) {
            this.context.close();
        }
    }
}
