package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT * FROM travel t, place p, place p2 " +
            "WHERE t.start_place = p.id " +
            "AND t.end_place = p2.id " +
            "AND ABS(p.latitude - :latitude) <= 0.05 " +
            "AND ABS(p.longitude - :longitude) <= 0.05 " +
            "AND ABS(p2.latitude - :latitude2) <= 0.05 " +
            "AND ABS(p2.longitude - :longitude2) <= 0.05 " +
            "AND TIMEDIFF(t.pick_up_datetime, :datetime) < \"00:30:00\"",
            nativeQuery = true)
    Page<Travel> match(Pageable pageable,
                       @Param("latitude") Double latitude,
                       @Param("longitude") Double longitude,
                       @Param("latitude2") Double latitude2,
                       @Param("longitude2") Double longitude2,
                       @Param("datetime") Instant datetime);

    @EntityGraph(attributePaths = "passengers")
    Page<Travel> findAllWithPassengersByPassengersLogin(Pageable pageable, String login);

    @EntityGraph(attributePaths = "passengers")
    Travel findOneWithPassengersByPassengersLoginAndId(String login, Long id);

}
