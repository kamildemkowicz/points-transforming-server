package points.transforming.app.server.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.measurement.api.MeasurementResponse;
import points.transforming.app.server.models.measurement.api.MeasurementRequest;
import points.transforming.app.server.services.measurement.CreateMeasurementResponseProvider;
import points.transforming.app.server.services.measurement.MeasurementResponseProvider;
import points.transforming.app.server.services.measurement.MeasurementService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private static final Logger logger = LoggerFactory.getLogger(MeasurementController.class);

    @GetMapping(params = {"!sort", "!page", "!size"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MeasurementResponse>> getAllMeasurements(final Principal principal) {
        return ResponseEntity.ok().body(this.measurementService.getAllMeasurement(principal.getName()));
    }

    @GetMapping(value = "/{measurementInternalId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MeasurementResponse> getMeasurement(@PathVariable final String measurementInternalId, final Principal principal) {
        return new MeasurementResponseProvider().doResponse(measurementService.getMeasurement(measurementInternalId, principal));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MeasurementResponse> createMeasurement(@RequestBody @Valid final MeasurementRequest measurementRequest, final Principal principal) {
        return new CreateMeasurementResponseProvider().doResponse(measurementService.createMeasurement(measurementRequest, principal));
    }

    @PostMapping(value = "/{internalMeasurementId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MeasurementResponse> updateMeasurement(@PathVariable final String internalMeasurementId,
                                                                 @RequestBody @Valid final MeasurementRequest measurementRequest, final Principal principal) {
        return new CreateMeasurementResponseProvider().doResponse(measurementService.updateMeasurement(internalMeasurementId, measurementRequest, principal));
    }
}
