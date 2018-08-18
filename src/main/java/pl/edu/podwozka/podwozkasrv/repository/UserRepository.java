package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.podwozka.podwozkasrv.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
}
