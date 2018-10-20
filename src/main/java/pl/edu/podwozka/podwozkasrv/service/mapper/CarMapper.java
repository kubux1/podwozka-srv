package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.Car;
import pl.edu.podwozka.podwozkasrv.service.dto.CarDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarMapper {

    public CarDTO carToCarDTO(Car car) {
        return new CarDTO(car);
    }

    public List<CarDTO> usersToUserDTOs(List<Car> cars) {
        return cars.stream()
                .filter(Objects::nonNull)
                .map(this::carToCarDTO)
                .collect(Collectors.toList());
    }

    public Car carDTOToCar(CarDTO carDTO) {
        if (carDTO == null) {
            return null;
        } else {
            Car car = new Car();
            car.setId(carDTO.getId());
            car.setDriverLogin(carDTO.getDriverLogin());
            car.setBrand(carDTO.getBrand());
            car.setModel(carDTO.getModel());
            car.setColor(carDTO.getColor());
            car.setProductionYear(carDTO.getProductionYear());
            car.setRegistrationNumber(carDTO.getRegistrationNumber());
            car.setMaxPassangersCapacity(carDTO.getMaxPassangersCapacity());
            return car;
        }
    }

    public List<Car> carDTOsToCars(List<CarDTO> carDTOs) {
        return carDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::carDTOToCar)
                .collect(Collectors.toList());
    }

    public Car carFromId(Long id) {
        if (id == null) {
            return null;
        }
        Car car = new Car();
        car.setId(id);
        return car;
    }
}
