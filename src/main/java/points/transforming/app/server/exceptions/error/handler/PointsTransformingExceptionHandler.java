package points.transforming.app.server.exceptions.error.handler;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import points.transforming.app.server.exceptions.PointsTransformingException;
import points.transforming.app.server.exceptions.error.ErrorMessageSourceResolvable;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_MESSAGE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class PointsTransformingExceptionHandler extends CommonsExceptionHandler {

    public PointsTransformingExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(PointsTransformingException.class)
    ResponseEntity<PointsTransformingErrorResponse> handlePointsTransformingException(final PointsTransformingException exception) {
        final String errorCode = provideErrorCode(exception);
        final String errorMessage = provideErrorMessage(exception);

        log.warn("handlePointsTransformingException: errorCode={}, errorMessage={}, exception", errorCode, errorMessage, exception);

        PointsTransformingErrorResponse.Builder builder = new PointsTransformingErrorResponse
            .Builder()
            .newError()
            .message(errorMessage)
            .errorCode(errorCode)
            .endError();

        if (exception.getBindingResult() != null) {
            prepareErrorResponse(exception.getBindingResult(), builder);
        }

        return ResponseEntity.status(exception.getHttpStatus()).body(builder.build());
    }

    private String provideErrorCode(final PointsTransformingException exception) {
        String errorCode = exception.getErrorCode();
        log.debug(ERROR_CODE_PLACEHOLDER, errorCode);
        if (StringUtils.isBlank(errorCode)) {
            errorCode = errorCodes.getMessage(new ErrorMessageSourceResolvable(exception, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());
            log.debug("errorCodes.getMessage={}", errorCode);
        }
        return errorCode;
    }

    private String provideErrorMessage(final PointsTransformingException exception) {
        String errorMessage = exception.getMessage();
        log.debug(MESSAGE_PLACEHOLDER, errorMessage);
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = errorMessages.getMessage(new ErrorMessageSourceResolvable(exception, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());
            log.debug("errorMessages.getMessage={}", errorMessage);
        }
        return errorMessage;
    }
}
