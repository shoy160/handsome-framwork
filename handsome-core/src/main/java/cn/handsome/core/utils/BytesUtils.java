package cn.handsome.core.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author shay
 * @date 2021/3/9
 */
public final class BytesUtils {
    private static final char[] BYTE2CHAR = new char[256];

    private static volatile boolean initTag = false;

    private static synchronized void initChars() {
        if (initTag) {
            return;
        }
        for (int i = 0; i < BYTE2CHAR.length; ++i) {
            BYTE2CHAR[i] = (char) i;
        }
        initTag = true;
    }

    public static String byteToString(byte[] data) {
        initChars();
        StringBuilder content = new StringBuilder();
        for (byte datum : data) {
            content.append(BYTE2CHAR[datum]);
        }
        return content.toString();
    }

    public static byte[] stringToBytes(String msg) {
        byte[] bytes = new byte[msg.length()];
        final char[] chars = msg.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    public static byte[] combineBytes(byte[] data, String other) {
        return combineBytes(data, stringToBytes(other));
    }

    public static byte[] combineBytes(byte[] data, byte[] other) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length + other.length);
        buffer.put(data);
        buffer.put(other);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        return new BigInteger(bytes).longValue();
    }

    public static String bytesToString(byte[] bytes, Charset charset) {
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        return new String(bytes, charset);
    }

    public static int bytesToInt(byte[] bytes) {
        int value = 0;
        int length = bytes.length;
        if (length > 4) {
            //防止溢出
            length = 4;
        }
        for (int i = length - 1; i >= 0; i--) {
            value += (bytes[i] & 0xFF) << (length - i - 1) * 8;
        }
        return value;
    }

    public static byte[] simple(byte[] bytes) {
        int start = 0;
        for (byte aByte : bytes) {
            if (aByte == 0) {
                start++;
            } else {
                break;
            }
        }
        byte[] buffer = new byte[bytes.length - start];
        if (bytes.length - start >= 0) {
            System.arraycopy(bytes, start, buffer, 0, bytes.length - start);
        }
        return buffer;
    }

    public static byte[] intToBytes(int value) {
        byte[] result = new byte[4];
        for (int i = 3; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    public static byte[] longToBytes(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }
}
