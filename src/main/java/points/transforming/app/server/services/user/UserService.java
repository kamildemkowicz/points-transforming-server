package points.transforming.app.server.services.user;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.user.RoleNotFoundException;
import points.transforming.app.server.exceptions.user.UserAlreadyExistsException;
import points.transforming.app.server.models.user.Role;
import points.transforming.app.server.models.user.RoleName;
import points.transforming.app.server.models.user.User;
import points.transforming.app.server.models.user.api.CreateUserRequest;
import points.transforming.app.server.repositories.RoleRepository;
import points.transforming.app.server.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public void createUser(final CreateUserRequest createUserRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(createUserRequest.getUsername()))) {
            throw new UserAlreadyExistsException(Error.USER_WITH_PROVIDED_USERNAME_ALREADY_EXIST_PTS501);
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(createUserRequest.getEmail()))) {
            throw new UserAlreadyExistsException(Error.USER_WITH_PROVIDED_EMAIL_ALREADY_EXIST_PTS502);
        }

        final var user = new User(createUserRequest.getFirstName(), createUserRequest.getLastName(), createUserRequest.getUsername(),
            createUserRequest.getEmail(), encoder.encode(createUserRequest.getPassword()));

        final var strRoles = createUserRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            if ("admin".equals(role)) {
                final Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RoleNotFoundException(Error.GIVEN_ROLE_DOES_NOT_EXIST_PTS503));
                roles.add(adminRole);
            } else {
                final Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException(Error.GIVEN_ROLE_DOES_NOT_EXIST_PTS503));
                roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);
    }

    enum Error {
        USER_WITH_PROVIDED_USERNAME_ALREADY_EXIST_PTS501, USER_WITH_PROVIDED_EMAIL_ALREADY_EXIST_PTS502, GIVEN_ROLE_DOES_NOT_EXIST_PTS503
    }
}
