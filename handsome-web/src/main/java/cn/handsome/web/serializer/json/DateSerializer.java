package cn.handsome.web.serializer.json;

import cn.handsome.core.enums.TimestampType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Date;

/**
 * @author shoy
 * @date 2021/6/24
 */
@RequiredArgsConstructor
public class DateSerializer extends JsonSerializer<Date> {
    private final TimestampType type;

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (null == value) {
            gen.writeNumber(-1);
        } else {
            long timestamp = type == TimestampType.Second ? value.getTime() / 1000 : value.getTime();
            gen.writeNumber(timestamp);
        }
    }
}
