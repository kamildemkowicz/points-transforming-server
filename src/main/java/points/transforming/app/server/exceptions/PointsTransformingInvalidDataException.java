package points.transforming.app.server.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PointsTransformingInvalidDataException extends PointsTransformingException {

    @Getter
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public PointsTransformingInvalidDataException(final Enum errorCode) {
        super(errorCode);
    }

    public PointsTransformingInvalidDataException(final String message) {
        super(message);
    }

    public PointsTransformingInvalidDataException(final String errorCode, final String message) {
        super(errorCode, message);
    }

    public PointsTransformingInvalidDataException(final Enum errorCode, final Exception e) {
        super(errorCode, e);
    }
}
