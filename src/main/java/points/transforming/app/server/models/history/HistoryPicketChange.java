package points.transforming.app.server.models.history;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.picket.PicketResponse;

@Builder
@Getter
public class HistoryPicketChange {
    private final PicketResponse picket;
    private final List<HistorySimpleChange> picketSimpleChanges;
    private final HistoryChangeType type;
}
