package cn.handsome.web.jwt;

import cn.handsome.core.enums.ResultCode;
import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.security.Token;
import cn.handsome.core.security.TokenClaims;
import cn.hutool.core.codec.Base64;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;

/**
 * 令牌实体
 *
 * @author shay
 * @date 2020/7/31
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenBuilder {

    private final Token token;

    public Token getToken() {
        return this.token;
    }

    private JWTCreator.Builder build(Date expiresAt) {
        //创建jwt
        JWTCreator.Builder builder = JWT.create();
        if (expiresAt != null) {
            builder.withExpiresAt(expiresAt);
        }
        //传入参数
        builder.withClaim(TokenClaims.ID, this.token.getId());
        builder.withClaim(TokenClaims.NAME, this.token.getName());
        builder.withClaim(TokenClaims.ROLE, this.token.getRole());
        builder.withClaim(TokenClaims.MARKING, this.token.getMarking());
        builder.withClaim(TokenClaims.CLAIMS, this.token.getClaims());
        return builder;
    }

    public static Token create(Map<String, Claim> map) {
        Token token = new Token();
        Claim idClaim = map.get(TokenClaims.ID);
        if (null != idClaim) {
            token.setId(idClaim.asString());
        }
        if (map.get(TokenClaims.NAME) != null) {
            token.setName(map.get(TokenClaims.NAME).asString());
        }
        if (map.get(TokenClaims.ROLE) != null) {
            token.setRole(map.get(TokenClaims.ROLE).asString());
        }
        if (map.get(TokenClaims.MARKING) != null) {
            token.setMarking(map.get(TokenClaims.MARKING).asString());
        }
        if (map.get(TokenClaims.CLAIMS) != null) {
            Map<String, Object> claims = map.get(TokenClaims.CLAIMS).asMap();
            token.setClaims(claims);
        }
        if (map.get(TokenClaims.EXP) != null) {
            Long expire = map.get(TokenClaims.EXP).asLong();
            token.setExp(expire);
        }
        return token;
    }

    private static Token verify(DecodedJWT jwt) {
        Date expiresAt = jwt.getExpiresAt();
        if (expiresAt != null && new Date().after(expiresAt)) {
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
        Map<String, Claim> map = jwt.getClaims();
        return create(map);
    }

    /**
     * JWT加密
     *
     * @param secret HS256 Secret
     * @return jwt
     */
    public String jwt(String secret) {
        return jwt(secret, null);
    }

    /**
     * JWT加密
     *
     * @param secret    HS256 Secret
     * @param expiresAt 过期时间
     * @return jwt
     */
    public String jwt(String secret, Date expiresAt) {
        try {
            //使用HMAC256进行加密
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //创建jwt
            JWTCreator.Builder builder = build(expiresAt);
            //签名加密
            return builder.sign(algorithm);
        } catch (Exception ex) {
            log.warn("jwt加密失败:{}", ex.getLocalizedMessage());
            return null;
        }
    }

    /**
     * JWT HS256校验
     *
     * @param jwtToken jwt
     * @param secret   JWT Secret
     * @return Token
     */
    public static Token verify(String jwtToken, String secret) {
        Algorithm algorithm;
        try {
            //使用HMAC256进行加密
            algorithm = Algorithm.HMAC256(secret);
            //解密
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(jwtToken);
            return verify(jwt);
        } catch (Exception ex) {
            log.warn("jwt校验失败:{}", ex.getLocalizedMessage());
            return null;
        }
    }

    public String jwtRsa(String privateKey, Date expiresAt) {
        try {
            //使用RSA进行加密
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] bytes = Base64.decode(privateKey.getBytes());
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
            Algorithm algorithm = Algorithm.RSA256(null, rsaPrivateKey);
            //创建jwt
            JWTCreator.Builder builder = build(expiresAt);
            //签名加密
            return builder.sign(algorithm);
        } catch (Exception ex) {
            log.warn("jwt RSA加密失败:{}", ex.getLocalizedMessage());
            return null;
        }
    }

    public String jwtRsa(String privateKey) {
        return jwtRsa(privateKey, null);
    }

    /**
     * JWT HS256校验
     *
     * @param jwtToken  jwt
     * @param publicKey JWT publicKey
     * @return Token
     */
    public static Token verifyRsa(String jwtToken, String publicKey) {
        Algorithm algorithm;
        try {
            //使用RSA进行解密
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] bytes = Base64.decode(publicKey.getBytes());
            EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) factory.generatePublic(keySpec);
            algorithm = Algorithm.RSA256(rsaPublicKey, null);
            //解密
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(jwtToken);
            return verify(jwt);
        } catch (Exception ex) {
            log.warn("jwt RSA校验失败:{}", ex.getLocalizedMessage());
            return null;
        }
    }
}
