package points.transforming.app.server.exceptions;

import java.io.IOException;

import lombok.Getter;

@Getter
public class PointsTransformingIOException extends IOException {

    private final String fieldName;
    private final Class requestName;
    private final Class expectedType;

    public PointsTransformingIOException(final String message, final String fieldName, final Class requestName, final Class expectedType) {
        super(message);
        this.fieldName = fieldName;
        this.requestName = requestName;
        this.expectedType = expectedType;
    }

    public PointsTransformingIOException(final String message) {
        super(message);
        fieldName = null;
        requestName = null;
        expectedType = null;
    }
}
