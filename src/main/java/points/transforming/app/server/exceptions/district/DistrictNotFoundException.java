package points.transforming.app.server.exceptions.district;

import org.springframework.http.HttpStatus;
import points.transforming.app.server.exceptions.PointsTransformingException;

public class DistrictNotFoundException extends PointsTransformingException {

    public DistrictNotFoundException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
