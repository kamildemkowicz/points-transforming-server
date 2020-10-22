package points.transforming.app.server.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import points.transforming.app.server.models.history.HistoryChanges;
import points.transforming.app.server.services.history.HistoryService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "history")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<HistoryChanges> getMeasurement(@PathVariable final String measurementInternalId) {
        return ResponseEntity.ok().body(historyService.getHistoryChanges(measurementInternalId));
    }
}
