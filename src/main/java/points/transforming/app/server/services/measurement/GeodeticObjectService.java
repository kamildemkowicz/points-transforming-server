package points.transforming.app.server.services.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectMappers;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectRequest;
import points.transforming.app.server.models.geodeticobject.api.GeodeticObjectResponse;
import points.transforming.app.server.models.geodeticobject.api.SingleLineResponse;
import points.transforming.app.server.models.picket.PicketResponse;
import points.transforming.app.server.repositories.GeodeticObjectRepository;
import points.transforming.app.server.services.picket.PicketService;

@Service
@AllArgsConstructor
public class GeodeticObjectService {

    private final GeodeticObjectRepository geodeticObjectRepository;
    private final SingleLineValidator singleLineValidator;
    private final PicketService picketService;

    public List<GeodeticObjectResponse> getGeodeticObjects(final String measurementInternalId) {
        final var geodeticObjects = geodeticObjectRepository.findAllByMeasurementInternalId(measurementInternalId);
        final var result = new ArrayList<GeodeticObjectResponse>();

        geodeticObjects.forEach(geodeticObject -> result.add(GeodeticObjectResponse.builder()
            .symbol(geodeticObject.getSymbol())
            .color(geodeticObject.getColor())
            .description(geodeticObject.getDescription())
            .name(geodeticObject.getName())
            .id(geodeticObject.getId())
            .singleLines(geodeticObject.getSingleLines().stream()
                .map(singleLine -> SingleLineResponse.of(
                    singleLine,
                    PicketResponse.of(picketService.getPicket(singleLine.getStartPicketInternalId())),
                    PicketResponse.of(picketService.getPicket(singleLine.getEndPicketInternalId())))
                )
                .collect(Collectors.toUnmodifiableList())
            )
            .build()));

        return result;
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
