package pl.edu.podwozka.podwozkasrv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.podwozka.podwozkasrv.domain.Car;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, String> {

    Optional<Car> findOneByDriverLogin(String login);
}
