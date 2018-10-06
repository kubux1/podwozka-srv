package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.podwozka.podwozkasrv.domain.PassengerTravel;

@Repository
public interface PassengerTravelRepository extends JpaRepository<PassengerTravel, Long> {

    Page<PassengerTravel> findAllByLogin(Pageable pageable, String login);

    PassengerTravel findOneById(Long id);

    void deleteOneById(Long id);

    void deleteByLogin(String login);
}
