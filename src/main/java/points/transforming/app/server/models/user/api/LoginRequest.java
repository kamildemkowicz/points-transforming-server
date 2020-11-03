package points.transforming.app.server.models.user.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    @NotBlank
    @Size(min=3, max = 60)
    private final String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private final String password;
}
