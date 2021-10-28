package cn.handsome.web.serializer.json;

import cn.handsome.core.enums.BaseEnum;
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
public class EnumDeserializer extends JsonDeserializer<BaseEnum> {
    private final BaseProperties config;

    @Override
    public BaseEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (config.isEnumValue()) {
            int value = jsonParser.getIntValue();
            return EnumUtils.getEnum(value, BaseEnum.class);
        } else {
            String name = jsonParser.getValueAsString();
            return EnumUtils.getEnum(name, BaseEnum.class);
        }
    }
}
