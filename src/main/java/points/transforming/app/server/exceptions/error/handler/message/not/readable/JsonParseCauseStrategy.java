package points.transforming.app.server.exceptions.error.handler.message.not.readable;

import java.util.Locale;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

@Component
public class JsonParseCauseStrategy implements HttpMessageNotReadableExceptionHandlingStrategy {

    @Override
    public boolean apply(final Throwable cause) {
        return cause instanceof JsonParseException;
    }

    @Override
    public PointsTransformingErrorResponse.Builder handle(final String message, final String errorCode, final String defaultErrorCode, final MessageSource errorCodes,
                                                          final MessageSource errorMessages, final HttpMessageNotReadableException e) {
        JsonParseException parseException = ((JsonParseException) e.getCause());
        String originalMessage = parseException.getOriginalMessage();

        String lineNr = String.valueOf(parseException.getLocation().getLineNr());
        String columnNr = String.valueOf(parseException.getLocation().getColumnNr());

        String exceptionMessage = e.getCause().getLocalizedMessage();

        String messageFromResource = errorMessages.getMessage(new DefaultMessageSourceResolvable(
            new String[] {errorCode}, new String[] {originalMessage, lineNr, columnNr}, exceptionMessage), Locale.getDefault());

        PointsTransformingErrorResponse.Builder builder = new PointsTransformingErrorResponse.Builder();

        return builder
            .newError()
            .message(messageFromResource)
            .errorCode(errorCode)
            .endError();
    }
}
