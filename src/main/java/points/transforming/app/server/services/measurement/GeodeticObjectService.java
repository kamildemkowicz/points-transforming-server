package points.transforming.app.server.services.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.GeodeticObjectNotFoundException;
import points.transforming.app.server.models.geodeticobject.api.*;
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

    public GeodeticObjectResponse createGeodeticObject(final GeodeticObjectRequest geodeticObjectRequest) {
        singleLineValidator.validateCreationRequest(geodeticObjectRequest.getSingleLines());

        final var geodeticObject = geodeticObjectRepository.save(GeodeticObjectMappers.toGeodeticObject(geodeticObjectRequest));

        return GeodeticObjectResponse.builder()
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
            .build();
    }

    public GeodeticObjectResponse updateGeodeticObject(final GeodeticObjectUpdateRequest geodeticObjectRequest) {
        singleLineValidator.validate(geodeticObjectRequest.getSingleLines());

        final var geodeticObject = geodeticObjectRepository.findById(geodeticObjectRequest.getId())
            .orElseThrow(() -> new GeodeticObjectNotFoundException(Error.GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401));

        final var geodeticObjectUpdated = geodeticObjectRepository.save(GeodeticObjectMappers.updateGeodeticObject(geodeticObject, geodeticObjectRequest));

        return GeodeticObjectResponse.builder()
            .symbol(geodeticObjectUpdated.getSymbol())
            .color(geodeticObjectUpdated.getColor())
            .description(geodeticObjectUpdated.getDescription())
            .name(geodeticObjectUpdated.getName())
            .id(geodeticObjectUpdated.getId())
            .singleLines(geodeticObjectUpdated.getSingleLines().stream()
                .map(singleLine -> SingleLineResponse.of(
                    singleLine,
                    PicketResponse.of(picketService.getPicket(singleLine.getStartPicketInternalId())),
                    PicketResponse.of(picketService.getPicket(singleLine.getEndPicketInternalId())))
                )
                .collect(Collectors.toUnmodifiableList())
            )
            .build();
    }

    public void deleteGeodeticObject(final int geodeticObjectId) {
        if (geodeticObjectRepository.existsById(geodeticObjectId)) {
            geodeticObjectRepository.deleteById(geodeticObjectId);
        } else {
            throw new GeodeticObjectNotFoundException(Error.GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401);
        }
    }

    enum Error {
        GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401
    }
}
