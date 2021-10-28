package cn.handsome.web.serializer.converter;

import cn.handsome.core.enums.BaseEnum;
import cn.handsome.core.utils.EnumUtils;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

/**
 * @author shoy
 * @date 2021/6/30
 */
public class EnumConverter implements Converter<Integer, BaseEnum> {

    @Override
    public BaseEnum convert(@NonNull Integer value) {
        return EnumUtils.getEnum(value, BaseEnum.class);
    }
}
