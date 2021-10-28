package cn.handsome.web.serializer.json;

import cn.handsome.core.enums.BaseEnum;
import cn.handsome.web.config.BaseProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * @author shoy
 * @date 2021/6/30
 */
@RequiredArgsConstructor
public class EnumSerializer extends JsonSerializer<BaseEnum> {
    private final BaseProperties config;

    @Override
    public void serialize(BaseEnum baseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (config.isEnumValue()) {
            jsonGenerator.writeNumber(baseEnum.getValue());
        } else {
            jsonGenerator.writeString(baseEnum.toString());
        }
    }
}
