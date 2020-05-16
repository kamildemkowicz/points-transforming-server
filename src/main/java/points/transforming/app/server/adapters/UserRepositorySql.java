package points.transforming.app.server.adapters;

import org.springframework.data.jpa.repository.JpaRepository;
import points.transforming.app.server.models.user.User;
import points.transforming.app.server.repositories.UserRepository;

public interface UserRepositorySql extends UserRepository, JpaRepository<User, Integer> { }
