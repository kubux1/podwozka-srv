package pl.edu.podwozka.podwozkasrv.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.podwozka.podwozkasrv.service.PassengerTravelService;
import pl.edu.podwozka.podwozkasrv.service.dto.PassengerTravelDTO;
import pl.edu.podwozka.podwozkasrv.web.rest.util.HeaderUtil;
import pl.edu.podwozka.podwozkasrv.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/passenger")
public class PassengerTravelResource {

    private final Logger log = LoggerFactory.getLogger(PassengerTravelResource.class);

    private final PassengerTravelService travelService;

    public PassengerTravelResource(PassengerTravelService travelService) {
        this.travelService = travelService;
    }

    /**
     * POST  /passenger/travels : Creates a new travel.
     * <p>
     * Creates a new travel if the login and email are not already used, and sends an
     * mail with an activation link.
     * The travel needs to be activated on creation.
     *
     * @param travelDTO the travel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new travel
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @return the ResponseEntity with status 400 (BAD REQUEST) if the travel has an id
     */
    @PostMapping("/travels")
    public ResponseEntity<PassengerTravelDTO> createPassengerTravel(@Valid @RequestBody PassengerTravelDTO travelDTO)
            throws URISyntaxException {
        log.debug("REST request to save PassengerTravel : {}", travelDTO);

        if (travelDTO.getId() != null) {
            return ResponseEntity.status(BAD_REQUEST).build();
        } else {
            PassengerTravelDTO newPassengerTravel = travelService.save(travelDTO);
            return ResponseEntity.created(new URI("/api/travels/" + newPassengerTravel.getId()))
                    .headers(HeaderUtil.createAlert("A travel is created with identifier ",
                            newPassengerTravel.getId().toString()))
                    .body(newPassengerTravel);
        }
    }

    /**
     * PUT /passenger/travels : Updates an existing PassengerTravel.
     *
     * @param travelDTO the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 404 (NOT_FOUND) if there is no travel with such id
     */
    @PutMapping("/travels")
    public ResponseEntity<PassengerTravelDTO> updatePassengerTravel(@Valid @RequestBody PassengerTravelDTO travelDTO) {
        log.debug("REST request to update PassengerTravel : {}", travelDTO);

        PassengerTravelDTO updatedPassengerTravel = travelService.findOne(travelDTO.getId());
        if (updatedPassengerTravel == null) {
            return ResponseEntity.notFound().build();
        }

        updatedPassengerTravel = travelService.save(travelDTO);

        return ResponseEntity.ok(updatedPassengerTravel);
    }

    /**
     * GET /passenger/travels : Updates an existing PassengerTravel.
     *
     * @param id the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 404 (NOT_FOUND) if there is no such travel
     */
    @GetMapping("/travels/{id}")
    public ResponseEntity<PassengerTravelDTO> getPassengerTravel(@PathVariable Long id) {
        log.debug("REST request to update PassengerTravel : {}", id);
        PassengerTravelDTO existingPassengerTravel = travelService.findOne(id);

        return existingPassengerTravel != null ? ResponseEntity.ok(existingPassengerTravel) :
                ResponseEntity.notFound().build();
    }

    /**
     * GET /passenger/travels : Updates an existing PassengerTravel.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels")
    public ResponseEntity<List<PassengerTravelDTO>>
    getPassengerTravel(Pageable pageable,
                       @RequestParam(required = true) String login,
                       @RequestParam(required = false) Long driverTravelId) {
        log.debug("REST request to update PassengerTravel : {}", login);
        Page<PassengerTravelDTO> page;
        if (driverTravelId != null) {
            page = travelService.findAllByDriverTravelIdAndLogin(pageable, driverTravelId, login);
        } else {
            page = travelService.findAllByLogin(pageable, login);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE /passenger/travels/{id} : delete the "id" PassengerTravel.
     *
     * @param id the id of the travel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/travels/delete/{id}")
    public ResponseEntity<Void> deletePassengerTravel(@PathVariable Long id) {
        log.debug("REST request to delete PassengerTravel: {}", id);
        travelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A travel is deleted with identifier ",
                id.toString())).build();
    }
}
