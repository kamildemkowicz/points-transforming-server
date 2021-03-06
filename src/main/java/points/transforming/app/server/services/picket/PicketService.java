package points.transforming.app.server.services.picket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.picket.Picket;
import points.transforming.app.server.repositories.PicketRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class PicketService {
    private final PicketRepository picketRepository;
    private final CoordinatesConversionService coordinatesConversionService;

    public Picket getPicket(final String picketInternalId) {
        return picketRepository.getByPicketInternalId(picketInternalId);
    }

    public void setPicketInternalIds(List<Picket> picketsToSave) {
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
        picketRepository.saveAll(pickets);
    }

    public AtomicInteger getHighestInternalId() {
        return new AtomicInteger(this.picketRepository.getHighestInternalId());
    }

    public void calculateCoordinatesToWgs84(final Measurement measurement) {
        measurement.getPickets().forEach(picket -> {
            if (!Objects.isNull(picket.getCoordinateX2000()) && !Objects.isNull(picket.getCoordinateY2000())) {
                final var wgs84Coordinates = coordinatesConversionService.convertCoordinateFromGeocentricToWgs84(picket, measurement.getDistrict().getZone());
                picket.setLongitude(wgs84Coordinates.getCoordinateY().doubleValue());
                picket.setLatitude(wgs84Coordinates.getCoordinateX().doubleValue());
            }
        });
    }

    public void calculateCoordinatesTo2000(final Measurement measurement) {
        measurement.getPickets().forEach(picket -> {
            if (Objects.isNull(picket.getCoordinateX2000()) && Objects.isNull(picket.getCoordinateY2000()) && Objects.nonNull(picket.getLatitude())
                && Objects.nonNull(picket.getLongitude())) {
                final var wgs84Coordinates = coordinatesConversionService.convertCoordinateFromWgs84ToGeocentric(picket, measurement.getDistrict().getZone());
                picket.setCoordinateX2000(wgs84Coordinates.getCoordinateX().doubleValue());
                picket.setCoordinateY2000(wgs84Coordinates.getCoordinateY().doubleValue());
            }
        });
    }
}
