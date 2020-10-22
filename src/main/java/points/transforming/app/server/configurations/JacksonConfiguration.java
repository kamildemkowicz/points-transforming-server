package points.transforming.app.server.configurations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import points.transforming.app.server.utils.serializers.*;

@Configuration
public class JacksonConfiguration {

    @Bean
    public SimpleModule module() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Boolean.class, new BooleanDeserializer());
        simpleModule.addDeserializer(boolean.class, new BooleanDeserializer());
        simpleModule.addDeserializer(String.class, new StringDeserializer());
        simpleModule.addDeserializer(Double.class, new DoubleDeserializer());
        simpleModule.addDeserializer(double.class, new DoubleDeserializer());
        simpleModule.addDeserializer(Long.class, new LongDeserializer());
        simpleModule.addDeserializer(long.class, new LongDeserializer());
        simpleModule.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        simpleModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        simpleModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        simpleModule.addDeserializer(Integer.class, new IntegerDeserializer());
        simpleModule.addDeserializer(int.class, new IntegerDeserializer());
        simpleModule.addDeserializer(LocalDate.class, new DateDeserializer());
        simpleModule.addSerializer(LocalDate.class, new DateSerializer());
        return simpleModule;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(final ObjectMapper jacksonObjectMapper) {
        jacksonObjectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
        jacksonObjectMapper.disable(MapperFeature.ALLOW_COERCION_OF_SCALARS);
        jacksonObjectMapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(jacksonObjectMapper);
        return converter;
    }
}
