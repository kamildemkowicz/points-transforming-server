package points.transforming.app.server.models.history;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HistoryChange {
    private List<HistorySimpleChange> measurementChanges;
    private List<HistoryPicketChange> picketChanges;
    private LocalDateTime dateTime;
}
