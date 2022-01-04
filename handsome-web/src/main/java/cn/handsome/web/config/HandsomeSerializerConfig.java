package cn.handsome.web.config;

import cn.handsome.core.Constants;
import cn.handsome.core.enums.BaseEnum;
import cn.handsome.core.enums.EnumSerializerType;
import cn.handsome.core.enums.TimestampType;
import cn.handsome.core.utils.ReflectUtils;
import cn.handsome.web.serializer.converter.DateLongConverter;
import cn.handsome.web.serializer.converter.DateStringConverter;
import cn.handsome.web.serializer.converter.EnumConverter;
import cn.handsome.web.serializer.json.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * 时间戳 配置
 *
 * @author shay
 * @date 2020/11/4
 */
@Configuration
public class HandsomeSerializerConfig {

    @Bean
    @ConditionalOnMissingBean
    public Converter<String, Date> dateConverter(BaseProperties config) {
        return new DateStringConverter(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public Converter<Integer, BaseEnum> enumConverter() {
        return new EnumConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public Converter<Long, Date> dateLongConverter(BaseProperties config) {
        return new DateLongConverter(config);
    }

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper(BaseProperties config) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        //忽略大小写
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        SimpleModule module = new SimpleModule();
        if (config.getEnumSerializer() != EnumSerializerType.String) {
            module.addSerializer(BaseEnum.class, new EnumSerializer(config));
            Set<Class<?>> enums = ReflectUtils.findClasses(Constants.BASE_PACKAGES, t -> BaseEnum.class.isAssignableFrom(t) && t.isEnum());
            for (Class<?> clazz : enums) {
                module.addDeserializer(clazz, new EnumDeserializer(config, clazz));
            }
        }

        //长整型处理
        if (config.isLongToString()) {
            module.addSerializer(Long.class, new LongSerializer());
            module.addSerializer(Long.TYPE, new LongSerializer());
        }
        objectMapper.registerModule(module);

        //时间类型处理
        TimestampType timestamp = config.getTimestamp();
        if (timestamp != TimestampType.None) {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(timestamp));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(timestamp));
            javaTimeModule.addSerializer(Date.class, new DateSerializer(timestamp));
            javaTimeModule.addDeserializer(Date.class, new DateDeserializer(timestamp));
            objectMapper.registerModule(javaTimeModule);
        } else {
            objectMapper.setDateFormat(new SimpleDateFormat(config.getDateFormat()));
        }
        //空值处理
        if (config.isEnableNullValue()) {
            HandsomeBeanSerializerModifier modifier = new HandsomeBeanSerializerModifier(config);
            SerializerFactory serializerFactory = objectMapper.getSerializerFactory().
                    withSerializerModifier(modifier);
            objectMapper.setSerializerFactory(serializerFactory);
        }
//        JsonUtils.setMapper(objectMapper);
        return objectMapper;
    }
}
