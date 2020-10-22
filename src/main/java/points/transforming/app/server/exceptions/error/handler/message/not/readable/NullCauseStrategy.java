package points.transforming.app.server.exceptions.error.handler.message.not.readable;

import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Component
public class NullCauseStrategy implements HttpMessageNotReadableExceptionHandlingStrategy {

    @Override
    public boolean apply(final Throwable cause) {
        return cause == null;
    }

    @Override
    public PointsTransformingErrorResponse.Builder handle(final String message, final String errorCode, final String defaultErrorCode,
                                                          final MessageSource errorCodes, final MessageSource errorMessages,
                                                          final HttpMessageNotReadableException e) {
        PointsTransformingErrorResponse.Builder builder = new PointsTransformingErrorResponse.Builder();
        return builder
            .newError()
            .message(message)
            .errorCode(errorCode)
            .endError();
    }
}
