package points.transforming.app.server.services.tachymetry;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.tachymetry.EmptyMeasuringStationsListException;
import points.transforming.app.server.models.tachymetry.api.TachymetryRequest;

@Component
@Slf4j
@AllArgsConstructor
public class TachymetryValidator {

    public void validateTachymetryRequest(final TachymetryRequest tachymetryRequest) {
        throwExceptionIfMeasuringStationsListIsEmpty(tachymetryRequest);
    }

    private void throwExceptionIfMeasuringStationsListIsEmpty(final TachymetryRequest tachymetryRequest) {
        log.debug("checkWhetherMeasuringStationsListIsNotEmpty: measurement internal ID={}", tachymetryRequest.getInternalMeasurementId());

        if (tachymetryRequest.getMeasuringStations().isEmpty()) {
            throw new EmptyMeasuringStationsListException(tachymetryRequest.getInternalMeasurementId());
        }
    }
}
