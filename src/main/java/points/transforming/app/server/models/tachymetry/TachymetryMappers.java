package points.transforming.app.server.models.tachymetry;

import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.tachymetry.api.MeasuringStationRequest;
import points.transforming.app.server.models.tachymetry.api.PicketMeasurementDataRequest;
import points.transforming.app.server.models.tachymetry.api.TachymetryMetaDataRequest;

public final class TachymetryMappers {
    public static Tachymetry toTachymetry(final Measurement measurement, final TachymetryMetaDataRequest tachymetryMetaDataRequest) {
        final var tachymetry = new Tachymetry();

        tachymetry.setMeasurementInternalId(measurement.getMeasurementInternalId());
        tachymetry.setName(tachymetryMetaDataRequest.getTachymetryName());
        tachymetry.setTachymetrType(tachymetryMetaDataRequest.getTachymetrType());
        tachymetry.setTemperature(tachymetryMetaDataRequest.getTemperature());
        tachymetry.setPressure(tachymetryMetaDataRequest.getPressure());

        return tachymetry;
    }

    public static MeasuringStation toMeasuringStation(final MeasuringStationRequest measuringStationRequest, final Tachymetry tachymetry) {
        final var measuringStation = new MeasuringStation();

        measuringStation.setStationName(measuringStationRequest.getStationName());
        measuringStation.setStationNumber(measuringStationRequest.getStationNumber());
        measuringStation.setTachymetry(tachymetry);

        return measuringStation;
    }

    public static TachymetryPicketMeasured toTachymetryPicketMeasured(final String picketInternalId,
                                                                      final PicketMeasurementDataRequest picketMeasurementDataRequest,
                                                                      final MeasuringStation measuringStation) {
        final var tachymetryPicketMeasured = new TachymetryPicketMeasured();

        tachymetryPicketMeasured.setPicketInternalId(picketInternalId);
        tachymetryPicketMeasured.setAngle(picketMeasurementDataRequest.getAngle().doubleValue());
        tachymetryPicketMeasured.setDistance(picketMeasurementDataRequest.getDistance().doubleValue());
        tachymetryPicketMeasured.setMeasuringStation(measuringStation);

        return tachymetryPicketMeasured;
    }
}
