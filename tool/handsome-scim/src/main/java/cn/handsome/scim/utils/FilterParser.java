package cn.handsome.scim.utils;

import cn.handsome.scim.model.ScimFilter;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
public class FilterParser {
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("^[a-zA-Z0-9.]+");
    private static final Pattern VALUE_PATTERN = Pattern.compile("^'([^']*)'");
    private static final String[] LOGICAL_OPERATORS = {"and", "or"};
    private static final String[] COMPARISON_OPERATORS = {"eq", "ne", "co", "sw", "ew", "gt", "lt", "ge", "le"};

    public static ScimFilter parse(String filter) {
        if (StrUtil.isBlank(filter)) {
            return null;
        }

        filter = filter.trim();
        Stack<ScimFilter> stack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        int index = 0;
        while (index < filter.length()) {
            char c = filter.charAt(index);

            if (Character.isWhitespace(c)) {
                index++;
                continue;
            }

            if (c == '(') {
                // 标记左括号
                stack.push(null);
                index++;
            } else if (c == ')') {
                // 处理括号内的表达式
                List<ScimFilter> nodes = new ArrayList<>();
                while (!stack.isEmpty() && stack.peek() != null) {
                    nodes.add(0, stack.pop());
                }
                stack.pop(); // 弹出左括号标记

                List<String> operators = new ArrayList<>();
                while (!operatorStack.isEmpty() && !isHigherPrecedenceOperator(operatorStack.peek(), null)) {
                    operators.add(0, operatorStack.pop());
                }

                ScimFilter combined = combineNodes(nodes, operators);
                stack.push(combined);
                index++;
            } else {
                // 检查是否是逻辑运算符
                String operator = checkOperator(filter, index, LOGICAL_OPERATORS);
                if (operator != null) {
                    while (!operatorStack.isEmpty() &&
                            !isHigherPrecedenceOperator(operatorStack.peek(), operator)) {
                        stack.push(combineNodes(stack.pop(), operatorStack.pop(), stack.pop()));
                    }
                    operatorStack.push(operator);
                    index += operator.length() + 1; // 跳过运算符和后续空格
                    continue;
                }

                // 解析属性
                Matcher attributeMatcher = ATTRIBUTE_PATTERN.matcher(filter.substring(index));
                if (!attributeMatcher.find()) {
                    throw new IllegalArgumentException("Invalid attribute at position " + index);
                }
                String attribute = attributeMatcher.group();
                index += attribute.length();

                // 跳过空格
                while (index < filter.length() && Character.isWhitespace(filter.charAt(index))) {
                    index++;
                }

                // 解析比较运算符
                String compOperator = checkOperator(filter, index, COMPARISON_OPERATORS);
                if (compOperator == null) {
                    throw new IllegalArgumentException("Invalid operator at position " + index);
                }
                index += compOperator.length() + 1; // 跳过运算符和后续空格

                // 解析值
                Matcher valueMatcher = VALUE_PATTERN.matcher(filter.substring(index));
                if (!valueMatcher.find()) {
                    throw new IllegalArgumentException("Invalid value at position " + index);
                }
                String value = valueMatcher.group(1);
                index += valueMatcher.group().length();

                // 创建比较节点
                stack.push(new ScimFilter(compOperator, null, null, attribute, value));
            }
        }

        // 处理剩余的运算符
        while (!operatorStack.isEmpty()) {
            stack.push(combineNodes(stack.pop(), operatorStack.pop(), stack.pop()));
        }

        return stack.isEmpty() ? null : stack.pop();
    }

    private static String checkOperator(String filter, int index, String[] operators) {
        for (String op : operators) {
            if (filter.regionMatches(true, index, op, 0, op.length())) {
                int nextIndex = index + op.length();
                if (nextIndex >= filter.length() || Character.isWhitespace(filter.charAt(nextIndex))) {
                    return op;
                }
            }
        }
        return null;
    }

    private static boolean isHigherPrecedenceOperator(String current, String next) {
        if (next == null) {
            return false;
        }
        // "and" 优先级高于 "or"
        return "and".equals(current) && "or".equals(next);
    }

    private static ScimFilter combineNodes(List<ScimFilter> nodes, List<String> operators) {
        if (nodes.isEmpty()) {
            return null;
        }
        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        ScimFilter result = nodes.get(0);
        for (int i = 0; i < operators.size(); i++) {
            result = new ScimFilter(operators.get(i), result, nodes.get(i + 1), null, null);
        }
        return result;
    }

    private static ScimFilter combineNodes(ScimFilter right, String operator, ScimFilter left) {
        return new ScimFilter(operator, left, right, null, null);
    }
}
