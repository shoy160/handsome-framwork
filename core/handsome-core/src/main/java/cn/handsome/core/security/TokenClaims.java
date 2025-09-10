package cn.handsome.core.security;

/**
 * @author shay
 * @date 2020/8/20
 */
public interface TokenClaims {
    String ID = "id";
    String NAME = "name";
    String ROLE = "role";
    String CLAIMS = "claims";
    String MARKING = "marking";
    String EXP = "exp";
}
