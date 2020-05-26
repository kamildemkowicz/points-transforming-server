package points.transforming.app.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.measurement.MeasurementReadModel;
import points.transforming.app.server.models.measurement.MeasurementWriteModel;
import points.transforming.app.server.services.MeasurementService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("measurements")
@MeasurementExceptionProcessing
public class MeasurementController {
    private final MeasurementService measurementService;
    private static final Logger logger = LoggerFactory.getLogger(MeasurementController.class);

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    public ResponseEntity<List<MeasurementReadModel>> getAllMeasurements() {
        return ResponseEntity.ok().body(this.measurementService.getAllMeasurement());
    }

    @GetMapping
    public ResponseEntity<List<MeasurementReadModel>> getAllMeasurements(Pageable page) {
        return ResponseEntity.ok().body(this.measurementService.getAllMeasurement(page));
    }

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<MeasurementReadModel> getMeasurement(@PathVariable String measurementInternalId) {
        return ResponseEntity.ok().body(this.measurementService.getMeasurement(measurementInternalId));
    }

    @PostMapping
    public ResponseEntity<MeasurementReadModel> createMeasurement(@RequestBody @Valid MeasurementWriteModel measurementWriteModel) {
        var result = measurementService.createMeasurement(measurementWriteModel);
        return ResponseEntity
                .created(URI.create("/" + result.getId()))
                .body(result);
    }

    @PostMapping(value = "/{internalMeasurementId}")
    public ResponseEntity<MeasurementReadModel> updateMeasurement(@PathVariable String internalMeasurementId, @RequestBody @Valid MeasurementWriteModel measurementWriteModel) {
        var result = measurementService.updateMeasurement(internalMeasurementId, measurementWriteModel);

        return ResponseEntity
                .created(URI.create("/" + result.getId()))
                .body(result);
    }
}
