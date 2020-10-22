package points.transforming.app.server.exceptions.error.handler.message.not.readable;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_MESSAGE;
import static points.transforming.app.server.exceptions.error.handler.CommonsExceptionHandler.*;

@Component
@Slf4j
public class JsonMappingCauseStrategy implements HttpMessageNotReadableExceptionHandlingStrategy {

    @Override
    public boolean apply(final Throwable cause) {
        return cause instanceof JsonMappingException;
    }

    @Override
    public PointsTransformingErrorResponse.Builder handle(final String providedMessage, final String providedErrorCode, final String defaultErrorCode, final MessageSource errorCodes,
                          final MessageSource errorMessages, final HttpMessageNotReadableException e) {
        PointsTransformingErrorResponse.Builder builder = new PointsTransformingErrorResponse.Builder();

        JsonMappingException jsonMappingException = (JsonMappingException) e.getCause();

        Pattern pattern = Pattern.compile("([\\w\\.]+)\\[\\\"(\\w+)\\\"\\]");

        Matcher matcher = pattern.matcher(jsonMappingException.getPathReference());

        String errorCode = providedErrorCode;
        String message = providedMessage;

        if (matcher.find()) {
            String requestName = matcher.group(1);
            String fieldName = matcher.group(2);

            String[] messageCodes = new String[] {
                JSON_MAPPING_PREFIX + requestName + "." + fieldName,
                JSON_MAPPING_PREFIX + requestName,
                requestName + "." + fieldName, requestName, JSON_MAPPING
            };

            errorCode = errorCodes.getMessage(new DefaultMessageSourceResolvable(messageCodes, null, defaultErrorCode), Locale.getDefault());
            log.debug(ERROR_CODE_PLACEHOLDER, errorCode);

            String originalMessage = jsonMappingException.getOriginalMessage();
            String lineNr = "";
            String columnNr = "";

            if (jsonMappingException.getLocation() != null) {
                lineNr = String.valueOf(jsonMappingException.getLocation().getLineNr());
                columnNr = String.valueOf(jsonMappingException.getLocation().getColumnNr());
            }

            String[] arguments = new String[] {originalMessage, lineNr, columnNr};

            message = errorMessages.getMessage(
                new DefaultMessageSourceResolvable(new String[] {errorCode}, arguments, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());
            log.debug(MESSAGE_PLACEHOLDER, message);
        }

        for (JsonMappingException.Reference reference : jsonMappingException.getPath()) {
            String fieldName = reference.getFieldName();
            String description = reference.getDescription();

            Matcher jsonMatcher = pattern.matcher(description);
            if (jsonMatcher.find()) {
                String requestName = jsonMatcher.group(1);

                String[] messageCodes = new String[] {
                    JSON_MAPPING_PREFIX + requestName + "." + fieldName, requestName + "." + fieldName,
                    JSON_MAPPING_PREFIX + requestName,
                    requestName,
                    JSON_MAPPING
                };

                String reason = errorMessages
                    .getMessage(new DefaultMessageSourceResolvable(messageCodes, null, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());
                log.debug("fieldName={}, reason={}", fieldName, reason);

                builder
                    .newError()
                    .message(message)
                    .errorCode(errorCode)
                    .field(fieldName)
                    .reason(reason)
                    .endError();
            }
        }
        return builder;
    }
}
