package points.transforming.app.server.exceptions.user;

import org.springframework.http.HttpStatus;
import points.transforming.app.server.exceptions.PointsTransformingException;

public class UserAlreadyExistsException extends PointsTransformingException {

    public UserAlreadyExistsException(final Enum enumError) {
        super(enumError);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
