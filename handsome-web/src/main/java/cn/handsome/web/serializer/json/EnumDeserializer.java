package cn.handsome.web.serializer.json;

import cn.handsome.core.enums.BaseEnum;
import cn.handsome.core.enums.EnumSerializerType;
import cn.handsome.core.utils.EnumUtils;
import cn.handsome.web.config.BaseProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * @author shoy
 * @date 2021/6/30
 */
@RequiredArgsConstructor
public class EnumDeserializer<T extends BaseEnum> extends JsonDeserializer<BaseEnum> {
    private final BaseProperties config;
    private final Class<T> clazz;

    @Override
    public BaseEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (config.getEnumSerializer() == EnumSerializerType.String) {
            String name = jsonParser.getValueAsString();
            return EnumUtils.getEnum(name, clazz);
        }
        int value = jsonParser.getIntValue();
        return EnumUtils.getEnum(value, clazz);
    }
}
