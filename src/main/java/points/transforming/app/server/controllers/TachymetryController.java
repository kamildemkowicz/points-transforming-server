package points.transforming.app.server.controllers;

import javax.validation.Valid;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TachymetryReportResponse> calculateTachymetry(@RequestBody @Valid final TachymetryRequest tachymetryRequest) {
        return new TachymetryResponseProvider().doResponse(
            tachymetryRequest.getInternalMeasurementId(),
            tachymetryService.calculateTachymetry(tachymetryRequest),
            tachymetryRequest.getTachymetryMetaData()
        );
    }

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<List<TachymetryReportResponse>> getAllMeasurements(@PathVariable final String measurementInternalId) {
        return new TachymetryResponseProvider().doResponse(tachymetryService.getTachymetries(measurementInternalId));
    }
}
