package points.transforming.app.server.controllers;

import javax.validation.Valid;

import java.security.Principal;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;
import points.transforming.app.server.models.tachymetry.api.TachymetryReportResponse;
import points.transforming.app.server.services.tachymetry.TachymetryResponseProvider;
import points.transforming.app.server.services.tachymetry.TachymetryService;

@RestController
@RequestMapping("tachymetry")
@AllArgsConstructor
public class TachymetryController {

    private final TachymetryService tachymetryService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TachymetryReportResponse> calculateTachymetry(@RequestBody @Valid final TachymetryRequest tachymetryRequest,
                                                                        final Principal principal) {
        return new TachymetryResponseProvider().doResponse(
            tachymetryRequest.getInternalMeasurementId(),
            tachymetryService.calculateTachymetry(tachymetryRequest, principal),
            tachymetryRequest.getTachymetryMetaData()
        );
    }

    @GetMapping(value = "/{measurementInternalId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<TachymetryReportResponse>> getAllMeasurements(@PathVariable final String measurementInternalId, final Principal principal) {
        return new TachymetryResponseProvider().doResponse(tachymetryService.getTachymetries(measurementInternalId, principal));
    }
}
