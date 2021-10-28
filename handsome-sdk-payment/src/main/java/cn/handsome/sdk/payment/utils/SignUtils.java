package cn.handsome.sdk.payment.utils;

import cn.handsome.core.enums.ValueEnum;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.hutool.core.convert.Convert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Todo
 *
 * @author shay
 * @date 2020/8/7
 */
public class SignUtils {
    private static Log log = LogFactory.getLog(SignUtils.class);

    public static String md5(String text) {
        if (text == null) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //update处理
            md.update(text.getBytes());
            //调用该方法完成计算
            byte[] messages = md.digest();

            int i;
            StringBuilder buf = new StringBuilder();
            for (byte b : messages) {
                //做相应的转化（十六进制）
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return text;
        }
    }

    public static String unEscape(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof ValueEnum) {
            return ((ValueEnum) value).getValue().toString();
        }
        return value.toString();
    }

    public static String signMap(Map<String, Object> map, String secret) {
        return signMap(map, secret, 0L);
    }

    public static String signMap(Map<String, Object> map, String secret, long timestamp) {
        //排除
        Map<String, Object> sortMap = new TreeMap<>(map);
        final String key = "sign";
        sortMap.remove(key);

        String unSign = MapUtils.toUrl(sortMap, null, true);
        if (timestamp <= 0) {
            timestamp = System.currentTimeMillis();
        }
        unSign = unSign.concat(secret).concat(String.valueOf(timestamp));
        log.info(String.format("unSign:%s", unSign));

        return String.format("%s%s", timestamp, md5(unSign).toLowerCase());
    }

    public static boolean verifyMap(Map<String, Object> map, String secret, String sign) {
        final int length = 45;
        if (CommonUtils.isEmpty(sign) || sign.length() != length) {
            log.error(String.format("支付验签失败，签名格式异常,sign:%s", sign));
            return false;
        }
        long timestamp = Convert.toLong(sign.substring(0, 13));
        String verifySign = signMap(map, secret, timestamp);
        boolean verify = verifySign.equals(sign);
        if (!verify) {
            log.error(String.format("支付验签失败，sign:%s,verify:%s", sign, verifySign));
        }
        return verify;
    }
}
