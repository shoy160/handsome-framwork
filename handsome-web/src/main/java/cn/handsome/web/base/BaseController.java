package cn.handsome.web.base;

import cn.handsome.core.Constants;
import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.core.enums.ResultCode;
import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.security.Token;
import cn.handsome.core.security.TokenSolver;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.web.security.AuthContext;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 基础控制器
 *
 * @author shay
 * @date 2020/7/15
 */
@Slf4j
public abstract class BaseController {
    @Resource
    private TokenSolver tokenSolver;

    /**
     * 生成Token
     *
     * @param token token
     * @param group 分组
     * @return jwt-token
     */
    protected String generateToken(Token token, String group) {
        return generateToken(token, group, false);
    }

    /**
     * 生成Token
     *
     * @param token  token
     * @param group  分组
     * @param single 是否开启单点登录
     * @return jwt-token
     */
    protected String generateToken(Token token, String group, boolean single) {
        return tokenSolver.generateToken(token, group, single);
    }

    /**
     * 生成Token
     *
     * @param token token
     * @return jwt-token
     */
    protected String generateToken(Token token) {
        return generateToken(token, Constants.EMPTY_STR);
    }

    protected HttpServletRequest getRequest() {
        return AuthContext.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return AuthContext.getResponse();
    }

    protected String getBody() {
        return getBody(Charset.defaultCharset());
    }

    protected String getBody(Charset charset) {
        byte[] buffer = AuthContext.getBody();
        if (buffer == null) {
            return null;
        }
        return new String(buffer, charset);
    }

    protected <T> T fromBody(Class<T> clazz) {
        return fromBody(clazz, Charset.defaultCharset());
    }

    protected <T> T fromBody(Class<T> clazz, Charset charset) {
        String body = getBody(charset);
        if (body == null) {
            return null;
        }
        return JsonUtils.json(body, clazz);
    }

    /**
     * 获取请求头
     *
     * @param key 请求头key
     * @return String
     */
    protected String getHeader(String key) {
        HttpServletRequest request = getRequest();
        return request.getHeader(key);
    }

    /**
     * 获取不为空的Token
     *
     * @return Token
     */
    protected Token currentRequiredToken() {
        Token token = currentToken();
        if (token == null) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        return token;
    }

    /**
     * 获取Token
     *
     * @return Token
     */
    protected Token currentToken() {
        Object tokenValue = getRequest().getAttribute(Constants.SESSION_TOKEN);
        if (tokenValue instanceof Token) {
            return (Token) tokenValue;
        }
        return null;
//        return tokenSolver.getToken();
    }

    /**
     * 当前用户ID
     *
     * @return id
     */
    protected Long currentId() {
        Token token = currentToken();
        if (token == null || CommonUtils.isEmpty(token.getId())) {
            return null;
        }
        return token.idAsLong();
    }

    /**
     * 当前用户ID,ID不能为空
     *
     * @return id
     */
    protected Long currentRequiredId() {
        Long id = currentId();
        if (CommonUtils.isEmpty(id)) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        return id;
    }

    /**
     * 当前用户ID
     *
     * @return id
     */
    protected Integer currentIdAsInteger() {
        Token token = currentToken();
        if (token == null || CommonUtils.isEmpty(token.getId())) {
            return null;
        }
        return token.idAsInteger();
    }

    /**
     * 当前用户ID,ID不能为空或0
     *
     * @return id
     */
    protected Integer currentRequiredIdAsInteger() {
        Integer id = currentIdAsInteger();
        if (id == null || id == 0) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        return id;
    }

    /**
     * 当前用户ID
     *
     * @return id
     */
    protected String currentIdAsString() {
        Token token = currentToken();
        if (token == null || CommonUtils.isEmpty(token.getId())) {
            return null;
        }
        return token.getId();
    }

    /**
     * 当前用户ID,ID不能为空或0
     *
     * @return id
     */
    protected String currentRequiredIdAsLong() {
        String id = currentIdAsString();
        if (CommonUtils.isEmpty(id)) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        return id;
    }

    protected ResultDTO success() {
        return ResultDTO.success(new Object());
    }

    protected <T> ResultDTO<T> success(T data) {
        return ResultDTO.success(data);
    }

    protected <T> ResultDTO<T> fail(ResultCode resultCode) {
        return ResultDTO.failT(resultCode);
    }

    protected <T> ResultDTO<T> fail(String message) {
        return ResultDTO.fail(ResultCode.FAILURE, message);
    }

    protected <T> ResultDTO<T> fail(String message, int code) {
        return ResultDTO.failT(code, message);
    }

    protected <T> ResultDTO<T> result(boolean status, String errorMsg) {
        return status ? success() : fail(errorMsg);
    }

    protected <T> T toBean(Object source, Class<T> clazz) {
        return CommonUtils.toBean(source, clazz);
    }

    protected <T> T toBean(Object source, Class<T> clazz, CopyOptions copyOptions) {
        return CommonUtils.toBean(source, clazz, copyOptions);
    }

    protected <T, TS> List<T> toListBean(List<TS> sourceList, Class<T> clazz) {
        return CommonUtils.toListBean(sourceList, clazz);
    }

    protected <T, TS> List<T> toListBean(List<TS> sourceList, Class<T> clazz, CopyOptions copyOptions) {
        return CommonUtils.toListBean(sourceList, clazz, copyOptions);
    }
}
