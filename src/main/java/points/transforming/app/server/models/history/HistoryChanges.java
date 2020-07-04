package points.transforming.app.server.models.history;

import java.util.List;

import lombok.*;

@Builder
@Getter
@Setter
public class HistoryChanges {
    private List<HistorySimpleChanges> changes;
}
