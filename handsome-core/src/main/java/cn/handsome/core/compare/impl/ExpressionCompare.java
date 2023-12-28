package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class ExpressionCompare extends BaseCompare {
    public ExpressionCompare() {
        super(ConditionOp.EXPRESSION);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
            Object result = engine.eval(String.valueOf(value));
            return Boolean.parseBoolean(String.valueOf(result));
        } catch (Exception e) {
            return false;
        }
    }
}
