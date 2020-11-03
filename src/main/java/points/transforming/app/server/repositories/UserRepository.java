package points.transforming.app.server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import points.transforming.app.server.models.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
