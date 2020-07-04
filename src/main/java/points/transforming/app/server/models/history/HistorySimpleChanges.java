package points.transforming.app.server.models.history;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HistorySimpleChanges {
    private List<HistoryChange> simpleChanges;
    private List<HistoryChange> picketChanges;
    private LocalDateTime dateTime;
}
