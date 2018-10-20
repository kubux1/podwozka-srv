package pl.edu.podwozka.podwozkasrv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.domain.Car;
import pl.edu.podwozka.podwozkasrv.repository.CarRepository;
import pl.edu.podwozka.podwozkasrv.service.dto.CarDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.CarMapper;

import java.util.Optional;

@Service
@Transactional
public class CarService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    /**
     * Save a car.
     *
     * @param carDTO the entity to save
     * @return the persisted entity
     */
    public CarDTO save(CarDTO carDTO) {
        Car car = carMapper.carDTOToCar(carDTO);
        Optional<Car> newCar = Optional.empty();
        // Far know we assume only that driver has only one car
        if (carRepository.findOneByDriverLogin(car.getDriverLogin()) == newCar) {
            log.debug("Request to save Car : {}", car);
            return new CarDTO(carRepository.save(car));
        } else {
            log.debug("Onwer already has assgined Car for : {}", car.getDriverLogin());
            return null;
        }
    }

    /**
     * Get a car
     *
     * @param login of the owner for a car
     * @return a car
     */
    @Transactional(readOnly = true)
    public Optional<Car> findCarByDriverLogin(String login) {
        log.debug("Request to get Car : {}", login);
        return carRepository.findOneByDriverLogin(login);
    }
}
