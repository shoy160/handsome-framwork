package cn.handsome.web.serializer.converter;

import cn.handsome.core.enums.TimestampType;
import cn.handsome.web.config.BaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/6/21
 */
@RequiredArgsConstructor
public class DateLongConverter implements Converter<Long, Date> {
    private final BaseProperties config;

    @Override
    public Date convert(Long value) {
        TimestampType timestamp = config.getTimestamp();
        if (timestamp == TimestampType.Second) {
            return new Date(value * 1000);
        }
        return new Date(value);
    }
}
