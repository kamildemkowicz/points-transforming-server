package points.transforming.app.server.models.history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HistoryChange {
    private String oldValue;
    private String newValue;
    private String label;
    private HistoryChangeType type;
}
