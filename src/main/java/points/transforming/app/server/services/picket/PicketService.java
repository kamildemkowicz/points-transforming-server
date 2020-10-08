package points.transforming.app.server.services.picket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.models.picket.PicketWriteModel;
import points.transforming.app.server.repositories.PicketRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class PicketService {
    private final PicketRepository picketRepository;
    private final CoordinatesConversionService coordinatesConversionService;

    public void setPicketInternalIds(List<PicketWriteModel> picketsToSave) {
        var highestMeasurementInternalId = new AtomicInteger(this.picketRepository.getHighestInternalId());
        picketsToSave.forEach(picket -> picket.setPicketInternalId("PIC-" + (highestMeasurementInternalId.incrementAndGet())));
    }

    public void setInternalIdsForNewPickets(List<Picket> pickets) {
        var highestMeasurementInternalId = new AtomicInteger(this.picketRepository.getHighestInternalId());

        pickets.forEach(picket -> {
            if (picket.getPicketInternalId() == null) {
                picket.setPicketInternalId("PIC-" + (highestMeasurementInternalId.incrementAndGet()));
            }
        });
    }

    public void savePickets(final List<Picket> pickets) {
        picketRepository.save(pickets);
    }

    public AtomicInteger getHighestInternalId() {
        return new AtomicInteger(this.picketRepository.getHighestInternalId());
    }

    public void calculateCoordinatesToWgs84(final Measurement measurement) {
        measurement.getPickets().forEach(picket -> {
            if (picket.getCoordinateX2000() != 0 && picket.getCoordinateY2000() != 0) {
                final var wgs84Coordinates = coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(picket, measurement.getDistrictId());
                picket.setCoordinateX(wgs84Coordinates.getCoordinateX().doubleValue());
                picket.setCoordinateY(wgs84Coordinates.getCoordinateY().doubleValue());
            }
        });
    }
}
