package points.transforming.app.server.exceptions.tachymetry;

public class ControlNetworkPointsException extends RuntimeException {
    private final String startingPointName;
    private final String endPointName;

    public ControlNetworkPointsException(final String startingPointName, final String endPointName) {
        this.startingPointName = startingPointName;
        this.endPointName = endPointName;
    }

    @Override
    public String toString() {
        return "Starting point "
            + startingPointName
            + " and end point "
            + endPointName
            + " cannot be the same.";
    }
}
