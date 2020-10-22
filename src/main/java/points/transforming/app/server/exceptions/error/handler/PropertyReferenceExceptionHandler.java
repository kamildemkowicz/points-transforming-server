package points.transforming.app.server.exceptions.error.handler;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class PropertyReferenceExceptionHandler extends CommonsExceptionHandler {

    public PropertyReferenceExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    ResponseEntity<PointsTransformingErrorResponse> handlePropertyReferenceException(final PropertyReferenceException exception) {
        log.debug("handlePropertyReferenceException", exception);

        String defaultErrorCode = provideDefaultErrorCode(new String[] {exception.getClass().getSimpleName()}, null);
        String message = errorMessages.getMessage(defaultErrorCode, new Object[] {exception.getPropertyName()}, Locale.getDefault());

        PointsTransformingErrorResponse response = new PointsTransformingErrorResponse
            .Builder()
            .newError()
            .message(message)
            .errorCode(defaultErrorCode)
            .endError()
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
