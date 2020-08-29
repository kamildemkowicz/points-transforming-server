package points.transforming.app.server.models.history;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import points.transforming.app.server.models.picket.PicketReadModel;

@Builder
@Getter
public class HistoryPicketChange {
    private final PicketReadModel picket;
    private final List<HistorySimpleChange> picketChanges;
    private final HistoryChangeType type;
}
