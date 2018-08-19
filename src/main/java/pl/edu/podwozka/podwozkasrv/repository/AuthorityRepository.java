package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.podwozka.podwozkasrv.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
