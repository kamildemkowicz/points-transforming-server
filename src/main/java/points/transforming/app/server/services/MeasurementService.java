package points.transforming.app.server.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.measurement.Measurement;
import points.transforming.app.server.models.measurement.MeasurementReadModel;
import points.transforming.app.server.models.measurement.MeasurementWriteModel;
import points.transforming.app.server.repositories.MeasurementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
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

    public MeasurementReadModel getMeasurement(int id) {
        Measurement measurement = this.measurementRepository
                .findById(id)
                .orElseThrow(() -> new MeasurementNotFoundException(id));

        return new MeasurementReadModel(measurement);
    }

    public MeasurementReadModel createMeasurement(MeasurementWriteModel measurementWriteModel) {
        Measurement measurement = this.measurementRepository.save(measurementWriteModel.toMeasurement());
        return new MeasurementReadModel(measurement);
    }
}
