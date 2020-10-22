package points.transforming.app.server.exceptions.error.handler.message.not.readable;

import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

public interface HttpMessageNotReadableExceptionHandlingStrategy {

    boolean apply(Throwable cause);

    PointsTransformingErrorResponse.Builder handle(String message, String errorCode, String defaultErrorCode, MessageSource errorCodes,
                                                   MessageSource errorMessages, HttpMessageNotReadableException e);

}
