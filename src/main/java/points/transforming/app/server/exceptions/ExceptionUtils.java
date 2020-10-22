package points.transforming.app.server.exceptions;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

    public static void throwsIllegalArgumentExceptionWhenNull(final Object object, final String message) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void throwsPointsTransformingInvalidDataExceptionWhenNull(final Object object, final String message) {
        if (Objects.isNull(object)) {
            throw new PointsTransformingInvalidDataException(message);
        }
    }

    public static PointsTransformingIOException throwsPointsTransformingIOExceptionWhenObjectIsWrongDeseralizable(final String message,
                                                                                                                  final String fieldName,
                                                                                                                  final Class requestName,
                                                                                                                  final Class expectedClass) {
        return new PointsTransformingIOException(message, fieldName, requestName, expectedClass);
    }
}
