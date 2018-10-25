package pl.edu.podwozka.podwozkasrv.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.podwozka.podwozkasrv.service.CarService;
import pl.edu.podwozka.podwozkasrv.service.dto.CarDTO;
import pl.edu.podwozka.podwozkasrv.web.rest.util.HeaderUtil;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.net.URI;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api")
public class CarResource {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);

    private final CarService carService;

    public CarResource(CarService carService) {
        this.carService = carService;
    }

    /**
     * POST  /cars : Creates a new car.
     * <p>
     * Creates a new car if the id is not already used and if owner has no car assigned
     *
     * @param carDTO the car to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new car.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @return the ResponseEntity with status 400 (BAD REQUEST) if the car has an id.
     * @return the ResponseEntity with status 403 (FORBIDDEN) if the owner already has a car.
     */
    @PostMapping("/cars")
    public ResponseEntity<CarDTO> createTravel(@Valid @RequestBody CarDTO carDTO) throws URISyntaxException {
        log.debug("REST request to save a Car : {}", carDTO);

        if (carDTO.getId() != null) {
            return ResponseEntity.status(BAD_REQUEST).build();
        } else {
            CarDTO newCar = carService.save(carDTO);
            if (newCar == null) {
                return ResponseEntity.status(FORBIDDEN).build();
            } else {
                return ResponseEntity.created(new URI("/api/cars/" + newCar.getId()))
                        .headers(HeaderUtil.createAlert("A car is created with identifier ",
                                newCar.getId().toString()))
                        .body(newCar);
            }
        }
    }

    /**
     * GET /cars/:login : get the a car.
     *
     * @param login the login of owner of a car.
     * @return the ResponseEntity with status 200 (OK) and with body car, or with status 404 (Not Found).
     */
    @GetMapping("/cars")
    public ResponseEntity<CarDTO> getCar(@RequestParam(required = true) String login) {
        log.debug("REST request to get a Car owned by: {}", login);
        return (carService.findCarByDriverLogin(login).map(CarDTO::new))
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /cars/restricted:login : get the car restricted info for other users than an owner of a car
     *
     * @param login the login of owner of a car.
     * @return the ResponseEntity with status 200 (OK) and with restricted body car info, or with status 404 (Not Found).
     */
    @GetMapping("/cars/restricted")
    public ResponseEntity<CarDTO> getCarRestricted(@RequestParam(required = true) String login) {
        log.debug("REST request to get a Car restricted info owned by: {}", login);
        return (carService.findCarByDriverLoginRestrictedInfo(login).map(CarDTO::new))
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(ResponseEntity.notFound().build());
    }
}
