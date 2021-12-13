package cn.handsome.tool.ipregion;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * IP 区域
 *
 * @author shay
 * @date 2021/12/07
 */
@Getter
public class IpRegion {
    private final int cityId;
    private final String region;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 运营商
     */
    private String operator;
    private final int dataPtr;

    private final static int REGION_LENGTH = 5;

    public IpRegion(int cityId, String region, int dataPtr) {
        this.cityId = cityId;
        this.region = region;
        this.dataPtr = dataPtr;
        if (StrUtil.isNotBlank(region)) {
            String[] array = region.split("\\|");
            if (array.length == REGION_LENGTH) {
                this.country = array[0];
                this.province = array[2];
                this.city = array[3];
                this.operator = array[4];
            }
        }
    }

    public IpRegion(int cityId, String region) {
        this(cityId, region, 0);
    }

    @Override
    public String toString() {
        return String.valueOf(cityId) + '|' + region + '|' + dataPtr;
    }
}
