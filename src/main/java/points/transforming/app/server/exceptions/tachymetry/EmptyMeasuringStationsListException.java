package points.transforming.app.server.exceptions.tachymetry;

public class EmptyMeasuringStationsListException extends RuntimeException {
    private final String measurementId;

    public EmptyMeasuringStationsListException(final String measurementId) {
        this.measurementId = measurementId;
    }

    @Override
    public String toString() {
        return "Request to create tachymetry for "
            + this.measurementId
            + " has empty measuring stations list.";
    }
}
