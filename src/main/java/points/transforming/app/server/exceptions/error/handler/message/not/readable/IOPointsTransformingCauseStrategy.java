package points.transforming.app.server.exceptions.error.handler.message.not.readable;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.PointsTransformingIOException;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_MESSAGE;
import static points.transforming.app.server.exceptions.error.handler.CommonsExceptionHandler.JSON_MAPPING;
import static points.transforming.app.server.exceptions.error.handler.CommonsExceptionHandler.JSON_MAPPING_PREFIX;

@Component
public class IOPointsTransformingCauseStrategy implements HttpMessageNotReadableExceptionHandlingStrategy {

    @Override
    public boolean apply(final Throwable cause) {
        return cause instanceof PointsTransformingIOException;
    }

    @Override
    public PointsTransformingErrorResponse.Builder handle(final String message, final String errorCode, final String defaultErrorCode, final MessageSource errorCodes,
                                                          final MessageSource errorMessages, final HttpMessageNotReadableException e) {
        PointsTransformingIOException ioException = ((PointsTransformingIOException) e.getCause());
        String fieldName = ioException.getFieldName();
        String requestName = ioException.getRequestName().getName();
        String expectedType = ioException.getExpectedType().getSimpleName();
        String[] messageCodes = new String[] {JSON_MAPPING_PREFIX + requestName + "." + fieldName,
            JSON_MAPPING_PREFIX + requestName, requestName + "." + fieldName, requestName, JSON_MAPPING};
        String errorCodeFromResource = errorCodes.getMessage(
            new DefaultMessageSourceResolvable(messageCodes, null, defaultErrorCode), Locale.getDefault());
        String messageFromResource = errorMessages.getMessage(
            new DefaultMessageSourceResolvable(new String[] {errorCodeFromResource}, null, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());
        String reason = errorMessages.getMessage(
            new DefaultMessageSourceResolvable(
                new String[] {expectedType + "." + fieldName, expectedType}, null, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());

        PointsTransformingErrorResponse.Builder builder = new PointsTransformingErrorResponse.Builder();
        return builder
            .newError()
            .message(messageFromResource)
            .errorCode(errorCodeFromResource)
            .field(fieldName)
            .reason(reason)
            .endError();
    }
}
