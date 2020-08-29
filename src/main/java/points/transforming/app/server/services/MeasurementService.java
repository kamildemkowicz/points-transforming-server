package points.transforming.app.server.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.MeasurementReadModel;
import points.transforming.app.server.models.measurement.MeasurementWriteModel;
import points.transforming.app.server.models.user.User;
import points.transforming.app.server.repositories.MeasurementRepository;
import points.transforming.app.server.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final UserRepository userRepository;
    private final PicketService picketService;

    public MeasurementService(MeasurementRepository measurementRepository, UserRepository userRepository, PicketService picketService) {
        this.measurementRepository = measurementRepository;
        this.userRepository = userRepository;
        this.picketService = picketService;
    }

    public List<MeasurementReadModel> getAllMeasurement() {
        return this.measurementRepository
                .findAll()
                .stream()
                .map(MeasurementReadModel::new)
                .collect(Collectors.toList());
    }

    public List<MeasurementReadModel> getAllMeasurement(Pageable page) {
        return this.measurementRepository
                .findAll(page)
                .getContent()
                .stream()
                .map(MeasurementReadModel::new)
                .collect(Collectors.toList());
    }

    public MeasurementReadModel getMeasurement(String measurementInternalId) {
        return new MeasurementReadModel(this.findMeasurementByInternalId(measurementInternalId));
    }

    public MeasurementReadModel createMeasurement(MeasurementWriteModel measurementWriteModel) {
        // TODO it will be fixed after authentication will be added
        Optional<User> user = userRepository.findById(1);
        picketService.setPicketInternalIds(measurementWriteModel.getPickets());
        int highestMeasurementInternalId = this.measurementRepository.getHighestInternalId();

        Measurement measurement = measurementWriteModel.toMeasurement(user.get());
        measurement.setMeasurementInternalId("MES-" + (highestMeasurementInternalId + 1));
        measurement.setVersion(1);

        return new MeasurementReadModel(this.measurementRepository.save(measurement));
    }

    public MeasurementReadModel updateMeasurement(String internalMeasurementId, MeasurementWriteModel newMeasurement) {
        // TODO it will be fixed after authentication will be added
        final Optional<User> user = userRepository.findById(1);

        final Measurement oldMeasurement = this.findMeasurementByInternalId(internalMeasurementId);
        final Measurement measurement = newMeasurement.toMeasurement(user.get());

        measurement.setVersion(oldMeasurement.getVersion() + 1);
        measurement.setMeasurementInternalId(oldMeasurement.getMeasurementInternalId());
        picketService.setInternalIdsForNewPickets(measurement.getPickets());

        final Measurement newMeasurementCreated = this.measurementRepository.save(measurement);
        oldMeasurement.setEndDate(newMeasurementCreated.getCreationDate());

        this.measurementRepository.save(oldMeasurement);

        return new MeasurementReadModel(newMeasurementCreated);
    }

    private Measurement findMeasurementByInternalId(String internalId) {
        return this.measurementRepository
                .findByMeasurementInternalIdAndEndDate(internalId, null)
                .orElseThrow(() -> new MeasurementNotFoundException(internalId));
    }
}
