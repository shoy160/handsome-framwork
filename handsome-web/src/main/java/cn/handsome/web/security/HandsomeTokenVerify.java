package cn.handsome.web.security;

import cn.handsome.core.cache.Cache;
import cn.handsome.core.enums.ResultCode;
import cn.handsome.core.security.Token;
import cn.handsome.core.security.TokenVerify;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.IdentityUtils;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/9/26
 */
@RequiredArgsConstructor
public class HandsomeTokenVerify implements TokenVerify {

    private final Cache<String, String> cache;
    private final static String DEFAULT_FORBIDDEN_VALUE = "1";

    @NacosValue(value = "${handsome.web.single_marking:^framework:token:marking}", autoRefreshed = true)
    private String singleMarking;

    @NacosValue(value = "${handsome.web.user_forbidden:^framework:token:forbidden}", autoRefreshed = true)
    private String userForbidden;

    @Override
    public boolean verifyPermission(Token token, String[] permissions) {
        //角色验证
        if (CommonUtils.isNotEmpty(permissions)) {
            String role = token.getRole();
            if (CommonUtils.isEmpty(role)) {
                return false;
            }
            String[] roles = role.split(",");
            return ArrayUtil.containsAny(roles, permissions);
        }
        return true;
    }

    private String groupKey(String prefix, String group, String id) {
        if (StrUtil.isBlank(group)) {
            return prefix.concat(":").concat(id);
        }
        return prefix.concat(":").concat(group).concat(":").concat(id);
    }

    @Override
    public boolean verifySingle(Token token, String group) {
        if (null == cache || StrUtil.isBlank(token.getMarking())) {
            return true;
        }
        String cacheKey = groupKey(singleMarking, group, token.getId());
        String marking = cache.get(cacheKey);
        if (StrUtil.isBlank(marking)) {
            return false;
        }
        if (!token.getMarking().equals(marking)) {
            // 登录已失效  恶意宣传
            throw ResultCode.UN_AUTHORIZED.exception("账号已在其他设备登录");
        }
        return true;
    }

    @Override
    public boolean verifyForbidden(Token token, String group) {
        String cacheKey = groupKey(userForbidden, group, token.getId());
        String reason = cache.get(cacheKey);
        if (StrUtil.isNotBlank(reason)) {
            if (DEFAULT_FORBIDDEN_VALUE.equals(reason)) {
                return false;
            } else {
                throw ResultCode.REQ_REJECT.exception(reason);
            }
        }
        return true;
    }

    @Override
    public void enableSingle(Token token, String group) {
        if (null == cache) {
            token.setMarking(null);
            return;
        }
        String cacheKey = groupKey(singleMarking, group, token.getId());
        if (StrUtil.isBlank(token.getMarking())) {
            token.setMarking(IdentityUtils.guid16());
        }
        cache.put(cacheKey, token.getMarking(), token.getExp() * 1000);
    }

    @Override
    public void logout(Token token, String group) {
        if (null == cache || null == token || StrUtil.isBlank(token.getMarking())) {
            return;
        }
        String cacheKey = groupKey(singleMarking, group, token.getId());
        cache.remove(cacheKey);
    }

    @Override
    public void forbidden(Object id, String group, String reason, Date expiredAt) {
        if (null == cache || null == id) {
            return;
        }
        String cacheKey = groupKey(userForbidden, group, id.toString());
        reason = StrUtil.isBlank(reason) ? DEFAULT_FORBIDDEN_VALUE : reason;
        if (null != expiredAt) {
            cache.put(cacheKey, reason, expiredAt);
        } else {
            cache.put(cacheKey, reason);
        }
    }
}
