package points.transforming.app.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "points.transforming")
@Getter
@Setter
public class PointsTransformingConfigProperties {

    private final Connection connection = new Connection();

    private String appBuild;
    private String applicationName;

    @Getter
    @Setter
    public static class Connection {
        private final Http http = new Http();

        @Getter
        @Setter
        public static class Http {
            private final Read read = new Read();
            private final Connect connect = new Connect();

            @Getter
            @Setter
            public static class Read {
                private final Timeout timeout = new Timeout();
            }

            @Getter
            @Setter
            public static class Connect {
                private final Timeout timeout = new Timeout();
            }

            @Getter
            @Setter
            public static class Timeout {
                private long milliseconds = 3000;
            }
        }
    }
}
