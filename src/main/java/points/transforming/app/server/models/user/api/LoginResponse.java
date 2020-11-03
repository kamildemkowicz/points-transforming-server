package points.transforming.app.server.models.user.api;

import java.util.Collection;



import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Builder
public class LoginResponse {
	private final String token;
	@Builder.Default
	private final String type = "Bearer";
	private final String username;
	private Collection<? extends GrantedAuthority> authorities;
}
