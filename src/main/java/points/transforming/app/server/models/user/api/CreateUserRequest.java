package points.transforming.app.server.models.user.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private final String firstName;

    @NotBlank
    @Size(min = 3, max = 50)
    private final String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    private final String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private final String email;

    private final Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private final String password;
}
