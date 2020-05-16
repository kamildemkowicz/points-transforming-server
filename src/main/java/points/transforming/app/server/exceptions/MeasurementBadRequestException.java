package points.transforming.app.server.exceptions;

public class MeasurementBadRequestException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Bad request. " + super.getMessage();
    }
}
