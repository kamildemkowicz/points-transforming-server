package points.transforming.app.server.exceptions;

import org.springframework.http.HttpStatus;

public class MeasurementNotFoundException extends PointsTransformingException {

    public MeasurementNotFoundException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
