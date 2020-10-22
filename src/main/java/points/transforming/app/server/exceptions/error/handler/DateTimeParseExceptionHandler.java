package points.transforming.app.server.exceptions.error.handler;

import java.time.format.DateTimeParseException;

import lombok.extern.slf4j.Slf4j;
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
public class DateTimeParseExceptionHandler extends CommonsExceptionHandler {

    public DateTimeParseExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<PointsTransformingErrorResponse> handleDateTimeParseException(final DateTimeParseException exception) {
        return handleException(exception, HttpStatus.BAD_REQUEST);
    }
}
