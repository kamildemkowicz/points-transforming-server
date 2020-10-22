package points.transforming.app.server.exceptions.tachymetry;

import org.springframework.http.HttpStatus;
import points.transforming.app.server.exceptions.PointsTransformingException;

public class EmptyMeasuringStationsListException extends PointsTransformingException {

    public EmptyMeasuringStationsListException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
