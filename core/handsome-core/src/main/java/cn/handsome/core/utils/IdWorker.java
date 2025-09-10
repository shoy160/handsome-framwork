package cn.handsome.core.utils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author shay
 * @date 2021/3/8
 * <p>名称：IdWorker.java</p>
 * <p>描述：分布式自增长ID</p>
 * <pre>
 *     Twitter的 Snowflake　JAVA实现方案
 * </pre>
 * 核心代码为其IdWorker这个类实现，其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用：
 * 1||0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，
 * 然后5位data center标识位，5位机器ID（并不算标识符，实际是为线程标识），
 * 然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
 * 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由data center和机器ID作区分），
 * 并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 * <p>
 * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
 */
@Slf4j
public class IdWorker {
    /**
     * 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
     */
    private final long ZONE_TICK;

    /**
     * 毫秒内自增位
     */
    private final static long SEQUENCE_BITS = 12L;
    /**
     * 机器ID偏左移12位
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据中心ID左移17位
     */
    private final long DATA_CENTER_ID_SHIFT;
    /**
     * 时间毫秒左移22位
     */
    private final long TIMESTAMP_LEFT_SHIFT;

    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 上次生产id时间戳
     */
    private static long lastTimestamp = -1L;
    /**
     * 并发控制
     */
    private long sequence = 0L;

    private final long workerId;
    /**
     * 数据标识id部分
     */
    private final long dataCenterId;

    public IdWorker() {
        this(1609430400000L, 5, 5, -1, -1);
    }

    public IdWorker(long workerId) {
        this(-1, workerId);
    }

    /**
     * @param workerId     工作机器ID
     * @param dataCenterId 序列号
     */
    public IdWorker(long dataCenterId, long workerId) {
        this(1609430400000L, 5, 5, dataCenterId, workerId);
    }

    public IdWorker(long zoneTick, long dataCenterId, long workerId) {
        this(zoneTick, 5, 5, dataCenterId, workerId);
    }

    /**
     * IdWorker
     *
     * @param zoneTick       zone
     * @param workBits       机器标识位数
     * @param dataCenterBits 数据中心标识位数
     * @param dataCenterId   dataCenterId
     * @param workerId       workerId
     */
    public IdWorker(long zoneTick, long workBits, long dataCenterBits, long dataCenterId, long workerId) {
        this.ZONE_TICK = zoneTick;
        // 机器ID最大值
        long maxWorkerId = ~(-1L << workBits);
        // 数据中心ID最大值
        long maxDataCenterId = ~(-1L << dataCenterBits);
        this.DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + workBits;
        this.TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + workBits + dataCenterBits;

        if (dataCenterId < 0) {
            dataCenterId = generateDataCenterId(maxDataCenterId);
        }
        if (workerId < 0) {
            workerId = generateWorkerId(dataCenterId, maxWorkerId);
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id should between 0 AND %d", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("data center Id should between 0 AND %d", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;

    }

    /**
     * 获取下一个ID
     *
     * @return id
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // sequence = 0L;
            // 4096
            sequence = RandomUtil.randomInt(100, 1000);
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID

        return ((timestamp - ZONE_TICK) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    public synchronized String nextStringId() {
        return Long.toString(nextId());
    }

    /**
     * 16进制
     *
     * @return id
     */
    public synchronized String nextString16Id() {
        return Long.toUnsignedString(nextId(), 16);
    }

    /**
     * 32进制
     *
     * @return id
     */
    public synchronized String nextString32Id() {
        return Long.toUnsignedString(nextId(), 32);
    }


    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * <p>
     * 获取 maxWorkerId
     * </p>
     */
    public static long generateWorkerId(long dataCenterId, long maxWorkerId) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            buffer.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (buffer.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * <p>
     * 数据标识id部分
     * </p>
     */
    public static long generateDataCenterId(long maxDataCenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDataCenterId + 1);
            }
        } catch (Exception e) {
            log.warn("Generate DataCenter Id Error: {}" + e.getMessage());
        }
        return id;
    }
}
