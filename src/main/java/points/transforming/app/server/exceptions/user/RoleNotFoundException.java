package points.transforming.app.server.exceptions.user;

import org.springframework.http.HttpStatus;
import points.transforming.app.server.exceptions.PointsTransformingException;

public class RoleNotFoundException extends PointsTransformingException {

    public RoleNotFoundException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
