package cn.handsome.web.filter;

import cn.handsome.core.Constants;
import cn.handsome.core.enums.ResultCode;
import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.security.Token;
import cn.handsome.core.security.TokenSolver;
import cn.handsome.core.security.TokenVerify;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.web.annotation.EnableAuth;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author shay
 * @date 2020/9/1
 */
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenSolver tokenSolver;
    private final TokenVerify tokenVerify;

    /**
     * 设置session
     *
     * @param request request
     * @param token   token
     */
    private void initSession(HttpServletRequest request, Token token) {

        if (null == request || null == token) {
            return;
        }
        request.setAttribute(Constants.SESSION_TOKEN, token);
        request.setAttribute(Constants.CLAIM_USER_ID, token.getId());
        request.setAttribute(Constants.CLAIM_USERNAME, token.getName());
        request.setAttribute(Constants.CLAIM_ROLE, token.getRole());
        Map<String, Object> claims = token.getClaims();
        if (null != claims) {
            for (String key : claims.keySet()) {
                request.setAttribute(Constants.CLAIM_PREFIX.concat(key), claims.get(key));
            }
        }
    }

    /**
     * Token校验
     *
     * @param auth auth
     * @return token
     */
    private Token verifyToken(EnableAuth auth) {
        if (null == tokenSolver) {
            return null;
        }
        String group = auth == null ? Constants.STR_EMPTY : auth.group();
        Token token = tokenSolver.getToken(group);
        if (token == null) {
            return null;
        }
        if (auth != null && !auth.anonymous()) {
            // 身份校验
            if (CommonUtils.isEmpty(token.getId())) {
                return null;
            }
            if (null != tokenVerify) {
                tokenVerify.verify(token, group, auth.roles());
            }
        }
        return token;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        //获取注解
        EnableAuth auth = method.getMethodAnnotation(EnableAuth.class);
        if (auth == null) {
            Class<?> clazz = method.getBeanType();
            auth = clazz.getAnnotation(EnableAuth.class);
            while (auth == null && Object.class != (clazz = clazz.getSuperclass())) {
                auth = clazz.getAnnotation(EnableAuth.class);
            }
        }
        Token token = verifyToken(auth);
        initSession(request, token);
        if (auth == null || auth.anonymous()) {
            return true;
        }
        if (token == null) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        return true;
    }
}
