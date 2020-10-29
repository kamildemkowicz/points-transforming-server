package points.transforming.app.server.exceptions.error.handler;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ControllerAdvice(basePackages = "points.transforming.app.server")
public class ConstraintViolationExceptionHandler extends CommonsExceptionHandler {

    public ConstraintViolationExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<PointsTransformingErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        log.debug("handle {}", exception.getClass().getSimpleName(), exception);

        final var defaultErrorCode = provideDefaultErrorCode(new String[] {exception.getClass().getSimpleName()}, null);
        final var reason = exception.getSQLException().getMessage();
        final var message = errorMessages.getMessage(defaultErrorCode, new Object[] {exception.getCause()}, Locale.getDefault());

        final var response = new PointsTransformingErrorResponse
            .Builder()
            .newError()
            .errorCode(defaultErrorCode)
            .message(message)
            .reason(reason)
            .endError()
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
