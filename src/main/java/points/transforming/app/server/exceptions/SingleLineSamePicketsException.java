package points.transforming.app.server.exceptions;

import org.springframework.http.HttpStatus;

public class SingleLineSamePicketsException extends PointsTransformingException {

    public SingleLineSamePicketsException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
