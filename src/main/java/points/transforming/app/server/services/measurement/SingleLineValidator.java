package points.transforming.app.server.services.measurement;

import java.util.List;

import org.springframework.stereotype.Component;
import points.transforming.app.server.exceptions.SingleLineSamePicketsException;
import points.transforming.app.server.models.geodeticobject.api.SingleLineRequest;

@Component
public class SingleLineValidator {

    public void validate(final List<SingleLineRequest> singleLineRequests) {
        singleLineRequests.forEach(this::validateIfStartPicketInternalIdIsTheSameLikeEndPicketInternalId);
    }

    private void validateIfStartPicketInternalIdIsTheSameLikeEndPicketInternalId(final SingleLineRequest singleLineRequest) {
        if (singleLineRequest.getStartPicketInternalId().equals(singleLineRequest.getEndPicketInternalId())) {
            throw new SingleLineSamePicketsException(Error.SINGLE_LINE_SAME_PICKETS_PTS400);
        }
    }

    enum Error {
        SINGLE_LINE_SAME_PICKETS_PTS400
    }
}
