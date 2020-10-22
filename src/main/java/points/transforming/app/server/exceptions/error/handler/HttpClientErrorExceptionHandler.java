package points.transforming.app.server.exceptions.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class HttpClientErrorExceptionHandler extends CommonsExceptionHandler {

    public HttpClientErrorExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<String> handleHttpClientErrorException(final HttpClientErrorException exception) {
        log.debug("handleHttpClientErrorException", exception);
        return new ResponseEntity<>(exception.getResponseBodyAsString(), exception.getStatusCode());
    }
}
