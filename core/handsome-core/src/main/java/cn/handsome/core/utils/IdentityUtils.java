package cn.handsome.core.utils;

import cn.handsome.core.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.UUID;

/**
 * @author shay
 * @date 2021/3/9
 */
@Slf4j
public final class IdentityUtils {
    private static final IdWorker ID_WORKER;
    private static final String UUID_SPLIT = "-";

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.NOVEMBER, 18);
        long zoneTick = calendar.getTime().getTime();
        ID_WORKER = new IdWorker(zoneTick, 5, 5, -1, -1);
//        ID_WORKER = new IdWorker();
    }

    /**
     * 有序UUID
     *
     * @return uuid
     */
    public static UUID guid() {
        byte[] mostBytes = BytesUtils.longToBytes(ID_WORKER.nextId());
        byte[] leastBytes = new byte[8];
        byte[] uid = UUID.randomUUID().toString().getBytes();
        leastBytes[0] = uid[0];
        leastBytes[1] = uid[1];
        leastBytes[2] = uid[2];
        leastBytes[3] = uid[3];
        leastBytes[4] = uid[4];
        leastBytes[5] = uid[5];
        leastBytes[6] = uid[6];
        leastBytes[7] = (byte) (0xc0 | (0xf & uid[7]));
        return new UUID(BytesUtils.bytesToLong(mostBytes), BytesUtils.bytesToLong(leastBytes));
    }

    public static String guid32() {
        return guid().toString().replaceAll(UUID_SPLIT, Constants.STR_EMPTY);
    }

    public static String guid16() {
        byte[] uid = UUID.randomUUID().toString().getBytes();
        long sum = 1L;
        for (byte b : uid) {
            sum = sum * (b + 1);
        }
        return Long.toHexString(sum - System.currentTimeMillis());
    }

    /**
     * 基于时间戳长整型ID
     *
     * @return id
     */
    public static long longId() {
        return ID_WORKER.nextId();
    }

    /**
     * 基于时间戳 - 10进制
     *
     * @return id
     */
    public static String stringId() {
        return ID_WORKER.nextStringId();
    }

    /**
     * 基于时间戳 - 16进制
     *
     * @return id
     */
    public static String string16Id() {
        return ID_WORKER.nextString16Id();
    }

    /**
     * 基于时间戳 - 32进制
     *
     * @return id
     */
    public static String string32Id() {
        return ID_WORKER.nextString32Id();
    }
}
