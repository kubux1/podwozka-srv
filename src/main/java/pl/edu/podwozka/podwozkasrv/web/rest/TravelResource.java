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
import pl.edu.podwozka.podwozkasrv.service.TravelService;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelUserDTO;
import pl.edu.podwozka.podwozkasrv.web.rest.util.HeaderUtil;
import pl.edu.podwozka.podwozkasrv.web.rest.util.PaginationUtil;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TravelResource {

    private final Logger log = LoggerFactory.getLogger(TravelResource.class);

    private final TravelService travelService;

    public TravelResource(TravelService travelService) {
        this.travelService = travelService;
    }

    /**
     * POST  /travels : Creates a new travel.
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
    public ResponseEntity<TravelDTO> createTravel(@Valid @RequestBody TravelDTO travelDTO) throws URISyntaxException {
        log.debug("REST request to save Travel : {}", travelDTO);

        if (travelDTO.getId() != null) {
            return ResponseEntity.status(BAD_REQUEST).build();
        } else {
            TravelDTO newTravel = travelService.save(travelDTO);
            return ResponseEntity.created(new URI("/api/travels/" + newTravel.getId()))
                    .headers(HeaderUtil.createAlert("A travel is created with identifier ",
                            newTravel.getId().toString()))
                    .body(newTravel);
        }
    }

    /**
     * PUT /travels : Updates an existing Travel.
     *
     * @param travelDTO the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 404 (NOT_FOUND) if there is no travel with such id
     */
    @PutMapping("/travels")
    public ResponseEntity<TravelDTO> updateTravel(@Valid @RequestBody TravelDTO travelDTO) {
        log.debug("REST request to update Travel : {}", travelDTO);

        TravelDTO updatedTravel = travelService.findOne(travelDTO.getId());
        if (updatedTravel == null) {
            return ResponseEntity.notFound().build();
        }

        updatedTravel = travelService.save(travelDTO);

        return ResponseEntity.ok(updatedTravel);
    }

    /**
     * GET /travels : Updates an existing Travel.
     *
     * @param id the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 404 (NOT_FOUND) if there is no such travel
     */
    @GetMapping("/travels/{id}")
    public ResponseEntity<TravelDTO> getTravel(@PathVariable Long id) {
        log.debug("REST request to update Travel : {}", id);
        TravelDTO existingTravel = travelService.findOne(id);

        return existingTravel != null ? ResponseEntity.ok(existingTravel) : ResponseEntity.notFound().build();
    }

    /**
     * GET /travels : Gets all travels by a login
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels")
    public ResponseEntity<List<TravelDTO>> getTravels(Pageable pageable, @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.findAllByLogin(pageable, login);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /travels : Gets all past travels
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/past")
    public ResponseEntity<List<TravelDTO>> getPastTravels(Pageable pageable,
                                                          @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.findAllPastByLogin(pageable, login, Instant.now());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels/past?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /travels/coming : Gets all coming travels
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/coming")
    public ResponseEntity<List<TravelDTO>> getComingTravels(Pageable pageable,
                                                            @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.findAllComingByLogin(pageable, login, Instant.now());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels/coming?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /travels/passenger/past : Gets all past travels for passenger
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/passenger/past")
    public ResponseEntity<List<TravelDTO>> getPastPassengerTravels(Pageable pageable,
                                                          @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.findAllForPassengerPastByLogin(pageable, login, Instant.now());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels/past?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /travels/passenger/coming : Gets all coming travels for passenger
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/passenger/coming")
    public ResponseEntity<List<TravelDTO>> getComingPassengerTravels(Pageable pageable,
                                                            @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.findAllForPassengerComingByLogin(pageable, login, Instant.now());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels/coming?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE /travels/{id} : delete the "id" Travel.
     *
     * @param id the id of the travel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/travels/delete/{id}")
    public ResponseEntity<Void> deleteTravel(@PathVariable Long id) {
        log.debug("REST request to delete Travel: {}", id);
        travelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A travel is deleted with identifier ",
                id.toString())).build();
    }

    /**
     * POST /travels/find : Returns travels matching a passenger criterion.
     *
     * @param pageable the pagination information
     * @param travelDTO the entity to match with
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @PostMapping("/travels/findMatching")
    public ResponseEntity<List<TravelDTO>> findTravel(Pageable pageable,
                                                      @Valid @RequestBody TravelDTO travelDTO) {
        log.debug("REST request to find travels matching passenger criterion");
        Page<TravelDTO> page = travelService.findTravelsForPassenger(pageable, travelDTO);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels/find"));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST /travels : Sign up a passenger for a travel.
     *
     * @param pageable the pagination information
     * @param login of the passenger
     * @param travelId to be signed up for
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @PostMapping("/travels/signUp")
    public ResponseEntity<Void> signUpForTravel(@RequestParam(required = true) String login,
                                                @RequestParam(required = true) Long travelId,
                                                @RequestParam(required = true) String startPlace,
                                                @RequestParam(required = true) String endPlace,
                                                @RequestParam(required = true) String pickUpDatetime) {
        log.debug("REST request to sign up for a Travel : {}", travelId);
        boolean noErrors = false;
        if (travelService.findOne(travelId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (travelService.checkIfUserSignedForTheSameTrip(login, travelId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (travelService.signUp(login, travelId)) {
            noErrors = travelService.addFields(login, travelId, startPlace, endPlace, pickUpDatetime);
        }
        return noErrors ? new ResponseEntity(HttpStatus.OK) : 
                new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * GET /travels : Get all the travels for which passanger signed up.
     *
     * @param pageable the pagination information
     * @param login of the passenger
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/passenger")
    public ResponseEntity<List<TravelDTO>> getPassengerTravels(Pageable pageable,
                                                               @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.passengerFindAllByLogin(pageable, login);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET /travels : Get all the travels for which passanger signed up.
     *
     * @param pageable the pagination information
     * @param login of the passenger
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/signedUpPassenger")
    public ResponseEntity<List<TravelUserDTO>> getPassengerByTravelId(Pageable pageable,
                                                               @RequestParam(required = true) String login,
                                                                  @RequestParam(required = true) Long travelId) {
        log.debug("REST request to find Acceptance : {}", login);
        Page<TravelUserDTO> page = travelService.findAcceptanceByTravelId(pageable, travelId);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET /travels : Get all the travels for which passanger signed up.
     *
     * @param pageable the pagination information
     * @param login of the passenger
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels/signedUpTravels")
    public ResponseEntity<List<TravelUserDTO>> getPassengerByTravelId(Pageable pageable,
                                                                      @RequestParam(required = true) String login,
                                                                      @RequestParam(required = true) String passenger) {
        log.debug("REST request to find Acceptance : {}", login);
        Page<TravelUserDTO> page = travelService.findAcceptanceByPassenger(pageable, passenger);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels?login=%b", login));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * PUT /travels : Updates an existing Travel.
     *
     * @param travelUserDTO the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 404 (NOT_FOUND) if there is no travel with such id
     */
    @PutMapping("/travels/signedUpPassenger")
    public ResponseEntity<TravelUserDTO> updateAcceptance(@Valid @RequestBody TravelUserDTO travelUserDTO) {
        log.debug("REST request to update Travel : {}", travelUserDTO);

        TravelUserDTO updatedTravelUser = travelService.findOneAcceptance(travelUserDTO.getTravelId(),
                travelUserDTO.getUserLogin());
        if (updatedTravelUser == null) {
            return ResponseEntity.notFound().build();
        }

        updatedTravelUser = travelService.changeAcceptanceStatusByTravelIdAndLogin(travelUserDTO.getTravelId(),
                travelUserDTO.getUserLogin(),
                travelUserDTO.isUserAccepted());

        return ResponseEntity.ok(updatedTravelUser);
    }
}
