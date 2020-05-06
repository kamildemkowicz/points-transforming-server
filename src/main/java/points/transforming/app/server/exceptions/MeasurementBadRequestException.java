package points.transforming.app.server.exceptions;

public class MeasurementBadRequestException extends RuntimeException {

    public MeasurementBadRequestException() { }

    @Override
    public String getMessage() {
        return "Bad request. " + super.getMessage();
    }
}
