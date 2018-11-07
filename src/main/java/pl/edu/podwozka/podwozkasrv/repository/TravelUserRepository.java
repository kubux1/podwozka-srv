package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.podwozka.podwozkasrv.domain.TravelUser;

@Repository
public interface TravelUserRepository extends JpaRepository<TravelUser, String> {

    Page<TravelUser> findAllByTravelId(Pageable pageable, Long travelId);

    TravelUser findFirstByTravelIdAndUserLogin(Long travelId, String login);
}
