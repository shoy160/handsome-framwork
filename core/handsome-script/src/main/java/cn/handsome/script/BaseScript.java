package cn.handsome.script;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.util.Assert;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2025/3/6
 */
public abstract class BaseScript implements IScript {
    private final String scriptName;
    private ScriptEngine engine;

    protected BaseScript(String scriptName) {
        this.scriptName = scriptName;
    }

    protected synchronized ScriptEngine getEngine() {
        if (Objects.nonNull(engine)) {
            return engine;
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName(scriptName);
        Assert.notNull(this.engine, String.format("script engine not found: %s", scriptName));
        return this.engine;
    }

    @Override
    public Object eval(String expression, Map<String, Object> variables) {
        CompiledScript compiledScript = createCompiledScript(resolveScript(expression));
        if (Objects.isNull(compiledScript)) {
            throw new RuntimeException("compiled script is null");
        }
        try {
            Object result;
            if (Objects.nonNull(variables) && !variables.isEmpty()) {
                Bindings bindings = engine.createBindings();
                bindings.putAll(variables);
                result = compiledScript.eval(bindings);
            } else {
                result = compiledScript.eval();
            }
            return recursionResult(result);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    protected String resolveScript(String script) {
        return String.format("(function(){return %s})()", script);
    }

    protected String doubleTransitionString(double num, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        //关闭科学计数法
        nf.setGroupingUsed(false);
        //定义保留几位小数
        nf.setMaximumFractionDigits(length);
        return nf.format(num);
    }

    private CompiledScript createCompiledScript(String script) {
        ScriptEngine engine = getEngine();
        if (engine instanceof Compilable) {
            Compilable compilable = (Compilable) engine;
            try {
                return compilable.compile(script);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    /**
     * 追加表达式生成的对象 ScriptObjectMirror 转换数组
     */
    private static Object recursionResult(Object sObject) {
        if (sObject instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObject = (ScriptObjectMirror) sObject;
            if (scriptObject.isArray()) {
                return parseArray(scriptObject);
            }
            return parseMap(scriptObject);
        }
        return getScriptValue(sObject);
    }

    private static List<Object> parseArray(ScriptObjectMirror scriptObject) {
        List<Object> arrayList = new ArrayList<>();
        for (Object value : scriptObject.values()) {
            Object obj = recursionResult(value);
            arrayList.add(obj);
        }
        return arrayList;
    }

    private static Map<String, Object> parseMap(ScriptObjectMirror scriptObject) {
        Map<String, Object> result = new HashMap<>(scriptObject.size());
        for (Map.Entry<String, Object> entry : scriptObject.entrySet()) {
            Object value = recursionResult(entry.getValue());
            result.put(entry.getKey(), value);
        }
        return result;
    }

    private static Object getScriptValue(Object value) {
        if (value instanceof Double) {
            double doubleValue = (double) value;
            if (doubleValue == Math.floor(doubleValue)) {
                if (doubleValue > Integer.MAX_VALUE) {
                    return Double.valueOf(doubleValue).longValue();
                }
                return Double.valueOf(doubleValue).intValue();
            }
            return BigDecimal.valueOf(doubleValue);
        }
        return value;
    }
}
