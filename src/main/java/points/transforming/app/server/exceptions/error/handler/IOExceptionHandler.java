package points.transforming.app.server.exceptions.error.handler;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class IOExceptionHandler extends CommonsExceptionHandler {

    public IOExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(IOException.class)
    ResponseEntity<PointsTransformingErrorResponse> handleIOException(final IOException exception) {
        log.debug("handleIOException", exception);

        if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(exception), "Broken pipe")) {
            return null;
        } else {
            return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
