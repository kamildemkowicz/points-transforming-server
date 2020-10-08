package points.transforming.app.server.exceptions.report;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViolationReport {
    private String cause;
    private String message;
    private String field;
}
