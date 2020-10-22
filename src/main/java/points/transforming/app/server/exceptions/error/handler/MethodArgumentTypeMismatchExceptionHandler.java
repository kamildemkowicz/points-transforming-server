package points.transforming.app.server.exceptions.error.handler;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_MESSAGE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "points.transforming.app.server")
@Slf4j
class MethodArgumentTypeMismatchExceptionHandler extends CommonsExceptionHandler {

    public MethodArgumentTypeMismatchExceptionHandler(final MessageSource errorCodes, final MessageSource errorMessages) {
        super(errorCodes, errorMessages);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<PointsTransformingErrorResponse> handleTypeMismatchException(final MethodArgumentTypeMismatchException exception,
                                                                                final HandlerMethod handlerMethod) {

        log.debug("handleTypeMismatchException", exception);

        String controllerTypeName = handlerMethod.getBeanType().getName();
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String parameterName = exception.getName();
        String requiredTypeName = exception.getRequiredType().getSimpleName();
        String errorMessageCode = exception.getErrorCode();

        String[] messageCodes = new String[] {
            errorMessageCode + "." + controllerName + "." + parameterName,
            errorMessageCode + "." + controllerTypeName + "." + parameterName,
            controllerTypeName + "." + parameterName,
            controllerName + "." + parameterName,
            controllerName,
            controllerTypeName
        };
        String[] messageArguments = new String[] {parameterName, requiredTypeName};
        log.debug("messageCodes={}", StringUtils.arrayToDelimitedString(messageCodes, ","));
        log.debug("messageArguments={}", StringUtils.arrayToDelimitedString(messageArguments, ","));
        log.debug("defaultMessage={}", exception.getLocalizedMessage());

        String reason = errorMessages.getMessage(
            new DefaultMessageSourceResolvable(messageCodes, messageArguments, exception.getLocalizedMessage()), Locale.getDefault());
        log.debug("reason={}", reason);

        String errorCode = provideDefaultErrorCode(messageCodes, messageArguments);
        log.debug(ERROR_CODE_PLACEHOLDER, errorCode);

        String message = errorMessages.getMessage(
            new DefaultMessageSourceResolvable(new String[] {errorCode}, null, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());
        log.debug(MESSAGE_PLACEHOLDER, message);

        PointsTransformingErrorResponse response = new PointsTransformingErrorResponse
            .Builder()
            .newError()
            .message(message)
            .errorCode(errorCode)
            .reason(reason)
            .endError()
            .build();

        return ResponseEntity.badRequest().body(response);
    }

}
