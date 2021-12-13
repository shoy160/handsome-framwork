package cn.handsome.tool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * IP 区域解析配置
 *
 * @author shay
 * @date 2021/12/07
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "handsome.ipregion")
public class IpRegionProperties {
    private final static int SIZE_STEP = 8;
    private final static int SIZE_UNIT = 2048;
    private final static int LENGTH = 12;

    private int totalHeaderSize = SIZE_STEP * SIZE_UNIT;
    private int indexBlockSize = SIZE_STEP * SIZE_UNIT / 2;
    private int indexBlockLength = LENGTH;
    /**
     * 数据地址
     */
    private String dbPath = "classpath:data/ip2region.db";
}
