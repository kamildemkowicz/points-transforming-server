package points.transforming.app.server.exceptions;

import org.springframework.http.HttpStatus;

public class GeodeticObjectNotFoundException extends PointsTransformingException {

    public GeodeticObjectNotFoundException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
