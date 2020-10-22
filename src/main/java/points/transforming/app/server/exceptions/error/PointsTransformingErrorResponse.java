package points.transforming.app.server.exceptions.error;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointsTransformingErrorResponse {

    private List<Error> errors = new ArrayList<>();

    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Getter
    @Setter
    public static class Error {

        String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String errorCode;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String field;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<Reason> reasons = new ArrayList<>();

        public Error message(final String message) {
            this.message = message;
            return this;
        }

        public Error errorCode(final String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Error field(final String field) {
            this.field = field;
            return this;
        }

        public Error reason(final String message) {
            this.getReasons().add(new Reason(message));
            return this;
        }
    }

    @Getter
    @Setter
    public static class Reason {

        private String message;

        public Reason(final String message) {
            this.message = message;
        }
    }

    public static class Builder {

        private PointsTransformingErrorResponse errorResponse;

        public Builder() {
            this.errorResponse = new PointsTransformingErrorResponse();
        }

        public ErrorBuilder newError() {
            return new ErrorBuilder(this);
        }

        public PointsTransformingErrorResponse build() {
            return errorResponse;
        }

        public static class ErrorBuilder {

            private Builder builder;
            private Error error;

            public ErrorBuilder(final Builder builder) {
                this.builder = builder;
                error = new Error();
                builder.errorResponse.errors.add(error);
            }

            public ErrorBuilder message(final String message) {
                error.message = message;
                return this;
            }

            public ErrorBuilder errorCode(final String errorCode) {
                error.errorCode = errorCode;
                return this;
            }

            public ErrorBuilder field(final String field) {
                error.field = field;
                return this;
            }

            public ErrorBuilder reason(final String message) {
                error.getReasons().add(new Reason(message));
                return this;
            }

            public Builder endError() {
                return builder;
            }
        }

    }
}
