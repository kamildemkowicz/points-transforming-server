package points.transforming.app.server.exceptions;

public class MeasurementNotFoundException extends RuntimeException {
    private final String measurementId;

    public MeasurementNotFoundException(int measurementId) {
        this.measurementId = String.valueOf(measurementId);
    }

    public MeasurementNotFoundException(String measurementId) {
        this.measurementId = measurementId;
    }

    public String toString() {
        return "Measurement " + this.measurementId + " does not exist";
    }
}
