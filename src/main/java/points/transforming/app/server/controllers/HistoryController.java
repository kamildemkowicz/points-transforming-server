package points.transforming.app.server.controllers;

import java.security.Principal;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import points.transforming.app.server.models.history.HistoryChanges;
import points.transforming.app.server.services.history.HistoryService;
import points.transforming.app.server.services.measurement.MeasurementService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "history")
public class HistoryController {
    private final HistoryService historyService;
    private final MeasurementService measurementService;

    @GetMapping(value = "/{measurementInternalId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<HistoryChanges> getMeasurement(@PathVariable final String measurementInternalId, final Principal principal) {
        measurementService.getMeasurement(measurementInternalId, principal);
        return ResponseEntity.ok().body(historyService.getHistoryChanges(measurementInternalId));
    }
}
