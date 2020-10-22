package points.transforming.app.server.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public abstract class PointsTransformingException extends RuntimeException {

    public static final String UNKNOWN_ERROR_CODE = "PTS000";
    public static final String UNKNOWN_ERROR_MESSAGE = "Internal Error";

    protected final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    private BindingResult bindingResult;
    private String errorCode;

    public PointsTransformingException() {
    }

    public PointsTransformingException(final String message) {
        super(message);
    }

    public PointsTransformingException(final String message, final Exception e) {
        super(message, e);
    }

    public PointsTransformingException(final Enum errorCode) {
        super();
        this.errorCode = enumNameToErrorCode(errorCode.name());
    }

    public PointsTransformingException(final Enum errorCode, final Exception e) {
        super(e);
        this.errorCode = enumNameToErrorCode(errorCode.name());
    }

    public PointsTransformingException(final String errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PointsTransformingException(final String errorCode, final String message, final Exception e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    private static String enumNameToErrorCode(final String s) {
        if (s.length() >= 8 && s.charAt(s.length() - 7) == '_') {
            return s.substring(s.length() - 6);
        }
        return UNKNOWN_ERROR_CODE;
    }

    @Override
    public String toString() {
        return super.toString()
            + '{'
            + "errorCode='" + errorCode + '\''
            + '}';
    }
}
