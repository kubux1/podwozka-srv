package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.podwozka.podwozkasrv.domain.Travel;

/**
 * Spring Data JPA repository for the Travel entity.
 */
@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    Page<Travel> findAllByLogin(Pageable pageable, String login);

    Travel findOneById(Long id);

    void deleteById(Long id);

    void deleteByLogin(String login);
}
