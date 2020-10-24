package points.transforming.app.server.services.measurement;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.api.MeasurementResponse;
import points.transforming.app.server.models.measurement.api.MeasurementRequest;
import points.transforming.app.server.models.user.User;
import points.transforming.app.server.repositories.MeasurementRepository;
import points.transforming.app.server.repositories.UserRepository;
import points.transforming.app.server.services.picket.PicketService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final UserRepository userRepository;
    private final PicketService picketService;
    private final DistrictService districtService;
    private final GeodeticObjectService geodeticObjectService;

    public List<MeasurementResponse> getAllMeasurement() {
        return this.measurementRepository
                .findAll()
                .stream()
                .map(MeasurementResponse::of)
                .collect(Collectors.toList());
    }

    public List<MeasurementResponse> getAllMeasurement(final Pageable page) {
        return this.measurementRepository
                .findAll(page)
                .getContent()
                .stream()
                .map(MeasurementResponse::of)
                .collect(Collectors.toList());
    }

    public Measurement getMeasurement(final String measurementInternalId) {
        return this.measurementRepository
            .findByMeasurementInternalIdAndEndDate(measurementInternalId, null)
            .orElseThrow(() -> new MeasurementNotFoundException(Error.MEASUREMENT_DOES_NOT_EXIST_PTS100));
    }

    @Transactional
    public Measurement createMeasurement(final MeasurementRequest measurementRequest) {
        // TODO it will be fixed after authentication will be added
        final Optional<User> user = userRepository.findById(1);
        final int highestMeasurementInternalId = this.measurementRepository.getHighestInternalId();

        final Measurement measurement = MeasurementMapper.toMeasurementFrom(measurementRequest);

        measurement.setUser(user.get());
        measurement.setDistrict(districtService.getDistrictById(measurementRequest.getDistrictId()));
        measurement.setMeasurementInternalId("MES-" + (highestMeasurementInternalId + 1));
        measurement.setVersion(1);

        picketService.setPicketInternalIds(measurement.getPickets());
        picketService.calculateCoordinatesToWgs84(measurement);
        // geodeticObjectService.createGeodeticObjects(measurementRequest.getGeodeticObjectRequests(), measurement.getMeasurementInternalId());

        return this.measurementRepository.save(measurement);
    }

    public Measurement updateMeasurement(final String internalMeasurementId, final MeasurementRequest measurementRequest) {
        // TODO it will be fixed after authentication will be added
        final Optional<User> user = userRepository.findById(1);

        final Measurement oldMeasurement = getMeasurement(internalMeasurementId);
        final Measurement measurement = MeasurementMapper.toMeasurementFrom(measurementRequest);

        measurement.setUser(user.get());
        measurement.setDistrict(districtService.getDistrictById(measurementRequest.getDistrictId()));
        measurement.setVersion(oldMeasurement.getVersion() + 1);
        measurement.setMeasurementInternalId(oldMeasurement.getMeasurementInternalId());

        picketService.setInternalIdsForNewPickets(measurement.getPickets());
        picketService.calculateCoordinatesToWgs84(measurement);
        // geodeticObjectService.createGeodeticObjects(measurementRequest.getGeodeticObjectRequests(), measurement.getMeasurementInternalId());

        final Measurement newMeasurementCreated = this.measurementRepository.save(measurement);
        oldMeasurement.setEndDate(newMeasurementCreated.getCreationDate());

        this.measurementRepository.save(oldMeasurement);

        return newMeasurementCreated;
    }

    enum Error {
        MEASUREMENT_DOES_NOT_EXIST_PTS100
    }
}
