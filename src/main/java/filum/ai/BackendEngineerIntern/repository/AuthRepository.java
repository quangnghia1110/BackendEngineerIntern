package filum.ai.BackendEngineerIntern.repository;

import filum.ai.BackendEngineerIntern.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(String role);
}
