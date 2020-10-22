package points.transforming.app.server.configurations;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;
import points.transforming.app.server.PointsTransformingConfigProperties;

@Configuration
@RequiredArgsConstructor
public class PointsTransformingConfiguration {

    private final PointsTransformingConfigProperties pointsTransformingConfigProperties;

    @Bean
    @DependsOn(value = "mappingJackson2HttpMessageConverter")
    RestTemplate restTemplate(final RestTemplateBuilder restTemplateBuilder, final JacksonConfiguration jacksonConfiguration, final ObjectMapper objectMapper) {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(pointsTransformingConfigProperties.getConnection().getHttp().getConnect().getTimeout().getMilliseconds()))
            .setReadTimeout(Duration.ofMillis(pointsTransformingConfigProperties.getConnection().getHttp().getRead().getTimeout().getMilliseconds()))
            .additionalMessageConverters(jacksonConfiguration.mappingJackson2HttpMessageConverter(objectMapper))
            .build();
    }
}
