package cn.handsome.tool.ipregion;

import cn.handsome.tool.config.IpRegionProperties;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * IP 区域查找器
 *
 * @author shay
 * @date 2021/12/07
 */
public class Searcher implements AutoCloseable {
    public static final int BTREE_ALGORITHM = 1;
    public static final int BINARY_ALGORITHM = 2;
    public static final int MEMORY_ALGORITHM = 3;

    private static final String CLASSPATH_TAG = "classpath:";

    private final IpRegionProperties config;
    private RandomAccessFile raf = null;
    private long[] headerSip = null;
    private int[] headerPtr = null;
    private int headerLength;
    private long firstIndexPtr = 0;
    private long lastIndexPtr = 0;
    private int totalIndexBlocks = 0;
    private byte[] dbBinStr = null;
    private final int blockLength;

    public Searcher(IpRegionProperties config) {
        this.config = config;
        this.blockLength = config.getIndexBlockLength();

        try {
            String path = config.getDbPath();
            if (path.startsWith(CLASSPATH_TAG)) {
                try (InputStream stream = ResourceUtil.getStream(path)) {
                    raf = toRandomAccessFile(stream);
                }
            } else {
                raf = new RandomAccessFile(config.getDbPath(), "r");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Searcher(IpRegionProperties config, byte[] dbBinStr) {
        this(config);
        this.dbBinStr = dbBinStr;
        firstIndexPtr = getIntLong(dbBinStr, 0);
        lastIndexPtr = getIntLong(dbBinStr, 4);
        totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / this.blockLength) + 1;
    }

    private static RandomAccessFile toRandomAccessFile(InputStream is) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(File.createTempFile("ip2region", "tmp"), "rwd");
        byte[] buffer = new byte[2048];
        int tmp = 0;
        while ((tmp = is.read(buffer)) != -1) {
            raf.write(buffer, 0, tmp);
        }
        raf.seek(0);
        return raf;
    }

    private static long getIntLong(byte[] b, int offset) {
        return (
                ((b[offset++] & 0x000000FFL)) |
                        ((b[offset++] << 8) & 0x0000FF00L) |
                        ((b[offset++] << 16) & 0x00FF0000L) |
                        ((b[offset] << 24) & 0xFF000000L)
        );
    }

    public static long ip2long(String ip) {
        String[] p = ip.split("\\.");
        if (p.length != 4) {
            return 0;
        }

        int p1 = ((Integer.parseInt(p[0]) << 24) & 0xFF000000);
        int p2 = ((Integer.parseInt(p[1]) << 16) & 0x00FF0000);
        int p3 = ((Integer.parseInt(p[2]) << 8) & 0x0000FF00);
        int p4 = ((Integer.parseInt(p[3])) & 0x000000FF);

        return ((p1 | p2 | p3 | p4) & 0xFFFFFFFFL);
    }

    private IpRegion getRegionFromMemory(long ptr) {
        if (ptr == 0) {
            return null;
        }
        int len = (int) ((ptr >> 24) & 0xFF);
        int dataPtr = (int) ((ptr & 0x00FFFFFF));
        int cityId = (int) getIntLong(dbBinStr, dataPtr);
        byte[] regionData = Arrays.copyOfRange(dbBinStr, dataPtr + 4, len - 4);
        String region = new String(regionData, StandardCharsets.UTF_8);
        return new IpRegion(cityId, region, dataPtr);
    }

    private IpRegion getRegion(long ptr) throws IOException {
        if (ptr == 0) {
            return null;
        }
        int len = (int) ((ptr >> 24) & 0xFF);
        int dataPtr = (int) ((ptr & 0x00FFFFFF));

        raf.seek(dataPtr);
        byte[] data = new byte[len];
        raf.readFully(data, 0, len);

        int cityId = (int) getIntLong(data, 0);
        String region = new String(data, 4, len - 4, StandardCharsets.UTF_8);
        return new IpRegion(cityId, region, dataPtr);
    }

    private IpRegion getRegionExtra(long ptr) throws IOException {
        raf.seek(ptr);
        byte[] buffer = new byte[12];
        raf.readFully(buffer, 0, buffer.length);
        long extra = getIntLong(buffer, 8);
        return getRegion(extra);
    }

    public IpRegion memorySearch(long ip) throws IOException {
        if (dbBinStr == null) {
            dbBinStr = new byte[(int) raf.length()];
            raf.seek(0L);
            raf.readFully(dbBinStr, 0, dbBinStr.length);

            //initialize the global vars
            firstIndexPtr = getIntLong(dbBinStr, 0);
            lastIndexPtr = getIntLong(dbBinStr, 4);
            totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / this.blockLength) + 1;
        }

        //search the index blocks to define the data
        int l = 0, h = totalIndexBlocks;
        long sip, eip, ptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            int p = (int) (firstIndexPtr + m * this.blockLength);

            sip = getIntLong(dbBinStr, p);
            if (ip < sip) {
                h = m - 1;
            } else {
                eip = getIntLong(dbBinStr, p + 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    ptr = getIntLong(dbBinStr, p + 8);
                    break;
                }
            }
        }

        return getRegionFromMemory(ptr);
    }

    public IpRegion memorySearch(String ip) throws IOException {
        return memorySearch(ip2long(ip));
    }


    public IpRegion bTreeSearch(long ip) throws IOException {
        //check and load the header
        if (headerSip == null) {
            //pass the super block
            raf.seek(8L);
            byte[] b = new byte[config.getTotalHeaderSize()];
            raf.readFully(b, 0, b.length);

            //fill the header
            //b.length / 8
            int len = b.length >> 3, idx = 0;
            headerSip = new long[len];
            headerPtr = new int[len];
            long startIp, dataPtr;
            for (int i = 0; i < b.length; i += 8) {
                startIp = getIntLong(b, i);
                dataPtr = getIntLong(b, i + 4);
                if (dataPtr == 0) {
                    break;
                }
                headerSip[idx] = startIp;
                headerPtr[idx] = (int) dataPtr;
                idx++;
            }

            headerLength = idx;
        }

        //1. define the index block with the binary search
        if (ip == headerSip[0]) {
            return getRegionExtra(headerPtr[0]);
        } else if (ip == headerSip[headerLength - 1]) {
            return getRegionExtra(headerPtr[headerLength - 1]);
        }

        int l = 0, h = headerLength, sptr = 0, eptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;

            //perfetc matched, just return it
            if (ip == headerSip[m]) {
                if (m > 0) {
                    sptr = headerPtr[m - 1];
                    eptr = headerPtr[m];
                } else {
                    sptr = headerPtr[m];
                    eptr = headerPtr[m + 1];
                }

                break;
            }

            //less then the middle value
            if (ip < headerSip[m]) {
                if (m == 0) {
                    sptr = headerPtr[m];
                    eptr = headerPtr[m + 1];
                    break;
                } else if (ip > headerSip[m - 1]) {
                    sptr = headerPtr[m - 1];
                    eptr = headerPtr[m];
                    break;
                }
                h = m - 1;
            } else {
                if (m == headerLength - 1) {
                    sptr = headerPtr[m - 1];
                    eptr = headerPtr[m];
                    break;
                } else if (ip <= headerSip[m + 1]) {
                    sptr = headerPtr[m];
                    eptr = headerPtr[m + 1];
                    break;
                }
                l = m + 1;
            }
        }

        //match nothing just stop it
        if (sptr == 0) {
            return null;
        }

        //2. search the index blocks to define the data
        int blockLen = eptr - sptr;
        //include the right border block
        byte[] iBuffer = new byte[blockLen + this.blockLength];
        raf.seek(sptr);
        raf.readFully(iBuffer, 0, iBuffer.length);

        l = 0;
        h = blockLen / this.blockLength;
        long sip, eip, ptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            int p = m * this.blockLength;
            sip = getIntLong(iBuffer, p);
            if (ip < sip) {
                h = m - 1;
            } else {
                eip = getIntLong(iBuffer, p + 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    ptr = getIntLong(iBuffer, p + 8);
                    break;
                }
            }
        }
        return getRegion(ptr);
    }

    public IpRegion bTreeSearch(String ip) throws IOException {
        return bTreeSearch(ip2long(ip));
    }

    public IpRegion binarySearch(long ip) throws IOException {
        if (totalIndexBlocks == 0) {
            raf.seek(0L);
            byte[] superBytes = new byte[8];
            raf.readFully(superBytes, 0, superBytes.length);
            //initialize the global vars
            firstIndexPtr = getIntLong(superBytes, 0);
            lastIndexPtr = getIntLong(superBytes, 4);
            totalIndexBlocks = (int) ((lastIndexPtr - firstIndexPtr) / this.blockLength) + 1;
        }

        //search the index blocks to define the data
        int l = 0, h = totalIndexBlocks;
        byte[] buffer = new byte[this.blockLength];
        long sip, eip, ptr = 0;
        while (l <= h) {
            int m = (l + h) >> 1;
            //set the file pointer
            raf.seek(firstIndexPtr + (long) m * this.blockLength);
            raf.readFully(buffer, 0, buffer.length);
            sip = getIntLong(buffer, 0);
            if (ip < sip) {
                h = m - 1;
            } else {
                eip = getIntLong(buffer, 4);
                if (ip > eip) {
                    l = m + 1;
                } else {
                    ptr = getIntLong(buffer, 8);
                    break;
                }
            }
        }
        return getRegion(ptr);
    }

    public IpRegion binarySearch(String ip) throws IOException {
        return binarySearch(ip2long(ip));
    }

    @Override
    public void close() throws IOException {
        //let gc do its work
        headerSip = null;
        headerPtr = null;
        dbBinStr = null;
        if (raf != null) {
            raf.close();
        }
    }
}
