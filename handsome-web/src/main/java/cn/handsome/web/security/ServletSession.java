package cn.handsome.web.security;

import cn.handsome.core.Constants;
import cn.handsome.core.session.BaseSession;
import cn.handsome.core.session.TenantSolver;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shoy
 * @date 2021/6/16
 */
@RequiredArgsConstructor
public class ServletSession extends BaseSession {
    private final TenantSolver tenantSolver;

    @Override
    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
        HttpServletRequest request = AuthContext.getRequest();
        if (request == null) {
            return null;
        }
        Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (name.startsWith(Constants.CLAIM_PREFIX)) {
                map.put(name.replace(Constants.CLAIM_PREFIX, Constants.STR_EMPTY), request.getAttribute(name));
            }
        }
        return map;
    }

    @Override
    protected Object getSessionUserId() {
        return getClaimValue(Constants.CLAIM_USER_ID);
    }

    @Override
    protected Object getSessionTenantId() {
        if (tenantSolver != null) {
            return tenantSolver.getTenantId();
        }
        return getClaimValue(Constants.CLAIM_TENANT_ID);
    }

    @Override
    protected Object getClaimValue(String key) {
        HttpServletRequest request = AuthContext.getRequest();
        if (request == null) {
            return null;
        }
        return request.getAttribute(key);
    }

    @Override
    protected void setClaimValue(String key, Object value) {
        HttpServletRequest request = AuthContext.getRequest();
        if (request == null) {
            return;
        }
        request.setAttribute(key, value);
    }
}
