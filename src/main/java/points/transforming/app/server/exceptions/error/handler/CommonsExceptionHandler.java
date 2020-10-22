package points.transforming.app.server.exceptions.error.handler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import points.transforming.app.server.exceptions.error.ErrorMessageSourceResolvable;
import points.transforming.app.server.exceptions.error.PointsTransformingErrorResponse;

import static java.util.stream.Collectors.toList;
import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_CODE;

@Slf4j
@RequiredArgsConstructor
public class CommonsExceptionHandler {

    public static final String ERROR_CODE_PLACEHOLDER = "errorCode={}";
    public static final String MESSAGE_PLACEHOLDER = "message={}";
    public static final String JSON_MAPPING_PREFIX = "jsonMapping.";
    public static final String JSON_MAPPING = "jsonMapping";

    protected final MessageSource errorCodes;
    protected final MessageSource errorMessages;

    void prepareErrorResponse(final BindingResult bindingResult, final PointsTransformingErrorResponse.Builder builder) {

        class ErrorCodeField {

            private String errorCode;
            private FieldError fieldError;

            private ErrorCodeField(final String errorCode, final FieldError fieldError) {
                this.errorCode = errorCode;
                this.fieldError = fieldError;
            }

            public String getErrorCode() {
                return errorCode;
            }

            private FieldError getFieldError() {
                return fieldError;
            }

            private String getFieldName() {
                return fieldError.getField();
            }
        }

        List<ErrorCodeField> errors = bindingResult
            .getFieldErrors()
            .stream()
            .map(fieldError ->
                new ErrorCodeField(
                    errorCodes.getMessage(new ErrorMessageSourceResolvable(fieldError, UNKNOWN_ERROR_CODE), Locale.getDefault()), fieldError)
            )
            .collect(toList());

        Map<String, List<ErrorCodeField>> groupedByErrorCode = errors.stream().collect(Collectors.groupingBy(ErrorCodeField::getErrorCode));

        groupedByErrorCode.forEach((errorCode, errorFields) -> {

            Map<String, List<ErrorCodeField>> groupedByFieldName = errorFields.stream().collect(Collectors.groupingBy(ErrorCodeField::getFieldName));

            groupedByFieldName.forEach((fieldName, fields) -> {

                PointsTransformingErrorResponse.Builder.ErrorBuilder errorBuilder = builder.newError();

                errorBuilder
                    .errorCode(errorCode)
                    .field(fieldName)
                    .message(errorMessages.getMessage(errorCode, null, Locale.getDefault()));

                fields.forEach(field -> {
                    String reason = errorMessages.getMessage(
                        new ErrorMessageSourceResolvable(field.getFieldError(), field.getFieldError().getDefaultMessage()), Locale.getDefault());
                    errorBuilder.reason(reason);
                });

            });

        });
    }

    ResponseEntity<PointsTransformingErrorResponse> handleException(final Exception exception, final HttpStatus httpStatus) {
        log.debug("handle {}", exception.getClass().getSimpleName(), exception);

        final var defaultErrorCode = provideDefaultErrorCode(new String[] {exception.getClass().getSimpleName()}, null);
        final var reason = exception.getLocalizedMessage();
        final var message = errorMessages.getMessage(defaultErrorCode, new Object[] {exception.getCause()}, Locale.getDefault());

        final var response = new PointsTransformingErrorResponse
            .Builder()
            .newError()
            .errorCode(defaultErrorCode)
            .message(message)
            .reason(reason)
            .endError()
            .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    protected String provideDefaultErrorCode(final @Nullable String[] codes, final @Nullable Object[] arguments) {
        return errorCodes.getMessage(
            new DefaultMessageSourceResolvable(codes, arguments, UNKNOWN_ERROR_CODE), Locale.getDefault());
    }
}
