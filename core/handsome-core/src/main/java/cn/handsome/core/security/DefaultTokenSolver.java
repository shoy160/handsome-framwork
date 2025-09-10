package cn.handsome.core.security;

/**
 * 默认 Token 获取器
 *
 * @author shay
 * @date 2021/3/4
 */
public class DefaultTokenSolver implements TokenSolver {
    @Override
    public Token getToken(String group) {
        return null;
    }

    @Override
    public String generateToken(Token token, String group, boolean single) {
        return "";
    }
}
