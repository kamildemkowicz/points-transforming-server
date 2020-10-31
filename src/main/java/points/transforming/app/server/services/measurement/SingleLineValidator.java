package points.transforming.app.server.services.measurement;

import java.util.List;

import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.SingleLineSamePicketsException;
import points.transforming.app.server.models.geodeticobject.api.SingleLineRequest;
import points.transforming.app.server.models.geodeticobject.api.SingleLineUpdateRequest;

@Component
public class SingleLineValidator {

    public void validateCreationRequest(final List<SingleLineRequest> singleLineRequests) {
        singleLineRequests.forEach(this::validateIfStartPicketInternalIdIsTheSameLikeEndPicketInternalId);
    }

    public void validate(final List<SingleLineUpdateRequest> singleLineRequests) {
        singleLineRequests.forEach(this::validateIfStartPicketInternalIdIsTheSameLikeEndPicketInternalId);
    }

    private void validateIfStartPicketInternalIdIsTheSameLikeEndPicketInternalId(final SingleLineRequest singleLineRequest) {
        if (singleLineRequest.getStartPicketInternalId().equals(singleLineRequest.getEndPicketInternalId())) {
            throw new SingleLineSamePicketsException(Error.SINGLE_LINE_SAME_PICKETS_PTS400);
        }
    }

    private void validateIfStartPicketInternalIdIsTheSameLikeEndPicketInternalId(final SingleLineUpdateRequest singleLineRequest) {
        if (singleLineRequest.getStartPicketInternalId().equals(singleLineRequest.getEndPicketInternalId())) {
            throw new SingleLineSamePicketsException(Error.SINGLE_LINE_SAME_PICKETS_PTS400);
        }
    }

    enum Error {
        SINGLE_LINE_SAME_PICKETS_PTS400
    }
}
