package points.transforming.app.server.configurations;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ErrorConfig {

    @Bean
    public MessageSource errorCodes() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBeanClassLoader(ErrorConfig.class.getClassLoader());
        messageSource.setBasename("errorcodes");
        return messageSource;
    }

    @Bean
    public MessageSource errorMessages() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBeanClassLoader(ErrorConfig.class.getClassLoader());
        messageSource.setBasename("errormessages");
        return messageSource;
    }
}
