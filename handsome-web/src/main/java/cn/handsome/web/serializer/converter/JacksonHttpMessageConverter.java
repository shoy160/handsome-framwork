package cn.handsome.web.serializer.converter;

import cn.handsome.web.config.BaseProperties;
import cn.handsome.web.serializer.json.HandsomeBeanSerializerModifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author shoy
 * @date 2021/6/7
 */
public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public JacksonHttpMessageConverter(BaseProperties config) {
        ObjectMapper mapper = getObjectMapper();
        HandsomeBeanSerializerModifier modifier = new HandsomeBeanSerializerModifier(config);
        SerializerFactory serializerFactory = mapper.getSerializerFactory().withSerializerModifier(modifier);
        mapper.setSerializerFactory(serializerFactory);
    }
}
