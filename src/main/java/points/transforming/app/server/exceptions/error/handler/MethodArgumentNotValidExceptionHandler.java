package points.transforming.app.server.exceptions.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class MethodArgumentNotValidExceptionHandler extends CommonsExceptionHandler {

    public MethodArgumentNotValidExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<PointsTransformingErrorResponse> handleNotValidException(final MethodArgumentNotValidException exception) {
        log.debug("handleNotValidException", exception);
        PointsTransformingErrorResponse.Builder builder = new PointsTransformingErrorResponse.Builder();
        prepareErrorResponse(exception.getBindingResult(), builder);
        return new ResponseEntity<>(builder.build(), HttpStatus.BAD_REQUEST);
    }

}
