package points.transforming.app.server.exceptions.error.handler;

import javax.validation.ConstraintViolationException;

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
public class ConstraintViolationExceptionHandler extends CommonsExceptionHandler {

    public ConstraintViolationExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<PointsTransformingErrorResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        return handleException(exception, HttpStatus.BAD_REQUEST);
    }
}
