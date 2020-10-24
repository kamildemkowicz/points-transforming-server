package points.transforming.app.server.services.measurement;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import points.transforming.app.server.models.geodeticobject.GeodeticObject;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectMappers;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectRequest;
import points.transforming.app.server.repositories.GeodeticObjectRepository;

@Service
@AllArgsConstructor
public class GeodeticObjectService {

    private final GeodeticObjectRepository geodeticObjectRepository;
    private final SingleLineValidator singleLineValidator;

    public List<GeodeticObject> getGeodeticObjects(final String measurementInternalId) {
        return geodeticObjectRepository.findAllByMeasurementInternalId(measurementInternalId);
    }

    public void createGeodeticObjects(final List<GeodeticObjectRequest> geodeticObjectRequest, final String measurementInternalId) {
        geodeticObjectRequest.forEach(geodeticObject -> singleLineValidator.validate(geodeticObject.getSingleLines()));

        final var geodeticObjects = geodeticObjectRequest.stream()
            .map(geodeticObject -> GeodeticObjectMappers.toGeodeticObject(geodeticObject, measurementInternalId))
            .collect(Collectors.toUnmodifiableList());

        geodeticObjectRepository.saveAll(geodeticObjects);
    }

    public void deleteGeodeticObject(final int geodeticObjectId) {
        geodeticObjectRepository.deleteById(geodeticObjectId);
    }
}
