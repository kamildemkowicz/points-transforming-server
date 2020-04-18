package points.transforming.app.server.exceptions;

public class MeasurementNotFoundException extends RuntimeException {
    private final int measurementId;

    public MeasurementNotFoundException(int measurementId) {
        this.measurementId = measurementId;
    }

    public String toString() {
        return "Measurement " + this.measurementId + " does not exist";
    }
}
