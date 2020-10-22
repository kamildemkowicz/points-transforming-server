package points.transforming.app.server.exceptions.error;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import points.transforming.app.server.exceptions.PointsTransformingException;

public class ErrorMessageSourceResolvable extends DefaultMessageSourceResolvable {

    public ErrorMessageSourceResolvable(final MessageSourceResolvable resolvable) {
        super(resolvable.getCodes());
    }

    public ErrorMessageSourceResolvable(final FieldError fieldError, final String defaultMessage) {
        super(StringUtils.concatenateStringArrays(fieldError.getCodes(), new String[] {fieldError.getObjectName() + "." + fieldError.getField(),
            fieldError.getObjectName()}), defaultMessage);
    }

    public ErrorMessageSourceResolvable(final Exception exception, final String defaultMessage) {
        super(exception.getCause() == null ? new String[] {exception.getClass().getName(), exception.getClass().getSimpleName()} :
            new String[] {exception.getCause().getClass().getName(), exception.getCause().getClass().getSimpleName()}, defaultMessage);
    }

    public ErrorMessageSourceResolvable(final PointsTransformingException exception, final String defaultMessage) {
        super(new String[] {exception.getErrorCode(), exception.getClass().getName(), exception.getClass().getSimpleName()}, defaultMessage);
    }
}
