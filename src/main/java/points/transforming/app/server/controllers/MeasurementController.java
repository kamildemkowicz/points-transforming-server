package points.transforming.app.server.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.measurement.MeasurementResponse;
import points.transforming.app.server.models.measurement.MeasurementRequest;
import points.transforming.app.server.services.measurement.CreateMeasurementResponseProvider;
import points.transforming.app.server.services.measurement.MeasurementResponseProvider;
import points.transforming.app.server.services.measurement.MeasurementService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private static final Logger logger = LoggerFactory.getLogger(MeasurementController.class);

    @GetMapping(params = {"!sort", "!page", "!size"})
    public ResponseEntity<List<MeasurementResponse>> getAllMeasurements() {
        return ResponseEntity.ok().body(this.measurementService.getAllMeasurement());
    }

    @GetMapping
    public ResponseEntity<List<MeasurementResponse>> getAllMeasurements(final Pageable page) {
        return ResponseEntity.ok().body(this.measurementService.getAllMeasurement(page));
    }

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<MeasurementResponse> getMeasurement(@PathVariable final String measurementInternalId) {
        return new MeasurementResponseProvider().doResponse(measurementService.getMeasurement(measurementInternalId));
    }

    @PostMapping
    public ResponseEntity<MeasurementResponse> createMeasurement(@RequestBody @Valid final MeasurementRequest measurementRequest) {
        return new CreateMeasurementResponseProvider().doResponse(measurementService.createMeasurement(measurementRequest));
    }

    @PutMapping(value = "/{internalMeasurementId}")
    public ResponseEntity<MeasurementResponse> updateMeasurement(@PathVariable final String internalMeasurementId,
                                                                 @RequestBody @Valid final MeasurementRequest measurementRequest) {
        return new CreateMeasurementResponseProvider().doResponse(measurementService.updateMeasurement(internalMeasurementId, measurementRequest));
    }
}
