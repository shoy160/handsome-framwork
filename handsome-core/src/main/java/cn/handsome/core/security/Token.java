package cn.handsome.core.security;

import cn.handsome.core.utils.CommonUtils;
import cn.hutool.core.convert.Convert;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shoy
 * @date 2021/9/27
 */
@Getter
@Setter
public class Token implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * name
     */
    private String name;
    /**
     * 角色
     */
    private String role;
    /**
     * 申明
     */
    private Map<String, Object> claims;
    /**
     * 过期时间戳(秒)
     */
    private Long exp;
    /**
     * 单点登录标记
     */
    private String marking;

    public Token() {
        claims = new HashMap<>();
    }

    public Token(Object id, String name) {
        this(id, name, null);
    }

    public Token(Object id, String name, String role) {
        if (id != null) {
            this.id = String.valueOf(id);
        }
        this.name = name;
        this.role = role;
        claims = new HashMap<>();
    }

    public <T> T getIdValue(Class<T> clazz) {
        if (CommonUtils.isEmpty(this.id)) {
            return null;
        }
        return Convert.convert(clazz, this.id);
    }

    public Integer idAsInteger() {
        return getIdValue(Integer.class);
    }

    public Long idAsLong() {
        return getIdValue(Long.class);
    }

    /**
     * 获取声明值
     *
     * @param key   key
     * @param clazz class
     * @param <T>   T
     * @return T
     */
    public <T> T getClaimValue(String key, Class<T> clazz) {
        if (claims == null || !claims.containsKey(key)) {
            return null;
        }
        Object value = claims.get(key);
        return value == null ? null : Convert.convert(clazz, value);
    }

    /**
     * 设置声明值
     *
     * @param key   key
     * @param value value
     */
    public void setClaimValue(String key, Object value) {
        if (claims.containsKey(key)) {
            claims.replace(key, value);
        } else {
            claims.put(key, value);
        }
    }
}
