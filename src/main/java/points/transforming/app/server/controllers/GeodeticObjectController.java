package points.transforming.app.server.controllers;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import points.transforming.app.server.models.geodeticobject.GeodeticObject;
import points.transforming.app.server.services.measurement.GeodeticObjectService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "geodeticObjects")
public class GeodeticObjectController {

    private final GeodeticObjectService geodeticObjectService;

    @GetMapping(value = "/{measurementInternalId}")
    public ResponseEntity<List<GeodeticObject>> getMeasurement(@PathVariable final String measurementInternalId) {
        return ResponseEntity.ok().body(geodeticObjectService.getGeodeticObjects(measurementInternalId));
    }
}
