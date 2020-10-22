package points.transforming.app.server.exceptions.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class ResourceAccessExceptionHandler extends CommonsExceptionHandler {

    public ResourceAccessExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(ResourceAccessException.class)
    ResponseEntity<PointsTransformingErrorResponse> handlePointsTransformingResourceAccessException(final ResourceAccessException exception) {
        return handleException(exception, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
