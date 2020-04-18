package points.transforming.app.server.service;

import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.Measurement;
import points.transforming.app.server.models.MeasurementRead;
import points.transforming.app.server.repositories.MeasurementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<MeasurementRead> getAllMeasurement() {
        return this.measurementRepository
                .findAll()
                .stream()
                .map(MeasurementRead::new)
                .collect(Collectors.toList());
    }

    public MeasurementRead getMeasurement(int id) {
        Measurement measurement = this.measurementRepository
                .findById(id)
                .orElseThrow(() -> new MeasurementNotFoundException(id));

        return new MeasurementRead(measurement);
    }
}
