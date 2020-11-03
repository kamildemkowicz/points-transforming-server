package points.transforming.app.server.services.user;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.models.user.Role;
import points.transforming.app.server.models.user.RoleName;
import points.transforming.app.server.models.user.User;
import points.transforming.app.server.models.user.api.CreateUserRequest;
import points.transforming.app.server.repositories.RoleRepository;
import points.transforming.app.server.repositories.UserRepository;

import static points.transforming.app.server.services.user.UserService.Error.GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public void createUser(final CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new MeasurementNotFoundException(GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401);
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new MeasurementNotFoundException(GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401);
        }

        final var user = new User(createUserRequest.getFirstName(), createUserRequest.getLastName(), createUserRequest.getUsername(),
            createUserRequest.getEmail(), encoder.encode(createUserRequest.getPassword()));

        final var strRoles = createUserRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    final Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "pm":
                    final Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                        .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    final Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);
    }

    enum Error {
        GEODETIC_OBJECT_DOES_NOT_EXIST_PTS401
    }
}
