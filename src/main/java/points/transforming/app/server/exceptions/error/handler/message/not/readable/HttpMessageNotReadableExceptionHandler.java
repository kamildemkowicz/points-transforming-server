package points.transforming.app.server.exceptions.error.handler.message.not.readable;

import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;
import points.transforming.app.server.exceptions.error.handler.CommonsExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class HttpMessageNotReadableExceptionHandler extends CommonsExceptionHandler {

    private final List<HttpMessageNotReadableExceptionHandlingStrategy> handlingStrategies;

    public HttpMessageNotReadableExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages,
                                                  final List<HttpMessageNotReadableExceptionHandlingStrategy> handlingStrategies) {
        super(errorCodes, errorMessages);
        this.handlingStrategies = handlingStrategies;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<PointsTransformingErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.debug("handleHttpMessageNotReadableException: {}", e);

        String[] defaultErrorCodes = new String[] {
            HttpMessageNotReadableException.class.getName(),
            HttpMessageNotReadableException.class.getSimpleName(),
        };
        if (e.getCause() != null) {
            defaultErrorCodes = StringUtils.concatenateStringArrays(
                new String[] {e.getCause().getClass().getName(), e.getCause().getClass().getSimpleName()}, defaultErrorCodes);
        }

        String defaultErrorCode = provideDefaultErrorCode(defaultErrorCodes, null);
        String defaultMessage = e.getMessage();
        String message = errorMessages.getMessage(new DefaultMessageSourceResolvable(
            new String[] {defaultErrorCode}, null, defaultMessage), Locale.getDefault());

        Throwable cause = e.getCause();

        PointsTransformingErrorResponse.Builder builder = handlingStrategies.stream()
            .filter(stategy -> stategy.apply(cause)).findFirst()
            .orElseThrow(() -> new UnsupportedOperationException("Strategy for given exception cause has not been implemented yet"))
            .handle(message, defaultErrorCode, defaultErrorCode, errorCodes, errorMessages, e);

        return ResponseEntity.badRequest().body(builder.build());
    }

}
