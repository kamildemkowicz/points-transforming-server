package points.transforming.app.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import points.transforming.app.server.models.MeasurementRead;
import points.transforming.app.server.service.MeasurementService;

import java.util.List;

@RestController("measurements")
public class MeasurementController {
    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    public ResponseEntity<List<MeasurementRead>> getAllMeasurements() {
        return ResponseEntity.ok().body(this.measurementService.getAllMeasurement());
    }
}
