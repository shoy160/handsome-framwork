package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import cn.hutool.core.util.StrUtil;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author shay
 * @date 2021/3/8
 */
public final class EncryptionUtil {

    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_CIPHER = "RSA/ECB/PKCS1Padding";

    public static String md5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] buffer = digest.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : buffer) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_STR;
    }


    private static EncodedKeySpec rsaKey(String key, boolean isEncrypt) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        byte[] keyData = Base64.getDecoder().decode(key);
        if (isEncrypt) {
            return new X509EncodedKeySpec(keyData);
        }
        return new PKCS8EncodedKeySpec(keyData);
    }

    /**
     * 使用RSA进行加密
     *
     * @param data      data
     * @param publicKey 公钥
     * @return byte[]
     */
    public static byte[] rsaEncryptByte(byte[] data, String publicKey) {
        try {
            EncodedKeySpec keySpec = rsaKey(publicKey, true);
            if (null == keySpec) {
                return null;
            }
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(keySpec));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 使用RSA进行加密
     *
     * @param data      数据
     * @param publicKey 公钥
     * @return 加密字符
     */
    public static String rsaEncrypt(String data, String publicKey) {
        byte[] encryptData = rsaEncryptByte(data.getBytes(StandardCharsets.UTF_8), publicKey);
        if (null == encryptData) {
            return Constants.EMPTY_STR;
        }
        return Base64.getEncoder().encodeToString(encryptData);
    }

    public static byte[] rsaDecryptByte(byte[] data, String privateKey) {
        try {
            EncodedKeySpec keySpec = rsaKey(privateKey, false);
            if (null == keySpec) {
                return null;
            }
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            Cipher cipher = Cipher.getInstance(RSA_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(keySpec));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String rsaDecrypt(String data, String privateKey) {
        byte[] bytes = Base64.getDecoder().decode(data);
        byte[] decryptData = rsaDecryptByte(bytes, privateKey);
        if (null == decryptData) {
            return Constants.EMPTY_STR;
        }
        return new String(decryptData, StandardCharsets.UTF_8);
    }
}
