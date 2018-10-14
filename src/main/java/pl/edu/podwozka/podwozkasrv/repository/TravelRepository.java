package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import org.springframework.data.jpa.repository.EntityGraph;
import java.time.Instant;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    Page<Travel> findAllByDriverLogin(Pageable pageable, String login);

    Travel findOneById(Long id);

    void deleteOneById(Long id);

    void deleteByDriverLogin(String login);

    Page<Travel> findAllByStartPlaceAndEndPlaceAndPickUpDatetimeGreaterThanEqual(Pageable pageable,
                                                                            String startPlace,
                                                                            String endPlace,
                                                                            Instant pickUpDatetime);

    @EntityGraph(attributePaths = "passengers")
    Page<Travel> findAllWithPassengersByPassengersLogin(Pageable pageable, String login);

}
