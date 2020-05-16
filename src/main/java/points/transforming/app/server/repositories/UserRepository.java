package points.transforming.app.server.repositories;

import points.transforming.app.server.models.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Integer id);
}
