package pl.edu.podwozka.podwozkasrv.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.podwozka.podwozkasrv.config.Constants;
import pl.edu.podwozka.podwozkasrv.service.TravelService;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.web.rest.util.HeaderUtil;
import static org.springframework.http.HttpStatus.CONFLICT;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class TravelResource {

    private final Logger log = LoggerFactory.getLogger(TravelResource.class);

    private final TravelService travelService;

    public TravelResource(TravelService travelService) {
        this.travelService = travelService;
    }

    /**
     * POST  /travels  : Creates a new travel.
     * <p>
     * Creates a new travel if the login and email are not already used, and sends an
     * mail with an activation link.
     * The travel needs to be activated on creation.
     *
     * @param travelDTO the travel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new travel, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @return the ResponseEntity with status 409 (CONFLICT) if the login or email is already in use
     */
    @PostMapping("/travels")
    public ResponseEntity<TravelDTO> createTravel(@Valid @RequestBody TravelDTO travelDTO) throws URISyntaxException {
        log.debug("REST request to save Travel : {}", travelDTO);

        TravelDTO existingUser = travelService.findOneByLogin(travelDTO.getLogin().toLowerCase());
        if (travelDTO.getId() != null) {
            return ResponseEntity.status(CONFLICT).build();
            // Lowercase the travel login before comparing with database
        } else if (existingUser != null && (!existingUser.getId().equals(travelDTO.getId()))) {
            return ResponseEntity.status(CONFLICT).build();
        } else {
            TravelDTO newTravel = travelService.save(travelDTO);
            return ResponseEntity.created(new URI("/api/travels/" + newTravel.getLogin()))
                    .headers(HeaderUtil.createAlert( "A travel is created with identifier " + newTravel.getLogin(), newTravel.getLogin()))
                    .body(newTravel);
        }
    }

    /**
     * PUT /users : Updates an existing Travel.
     *
     * @param travelDTO the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 409 (CONFLICT) if the login is already in use
     */
    @PutMapping("/travels")
    public ResponseEntity<TravelDTO> updateTravel(@Valid @RequestBody TravelDTO travelDTO) {
        log.debug("REST request to update Travel : {}", travelDTO);
        TravelDTO existingUser = travelService.findOneByLogin(travelDTO.getLogin().toLowerCase());
        if (existingUser != null && (!existingUser.getId().equals(travelDTO.getId()))) {
            return ResponseEntity.status(CONFLICT).build();
        }
        TravelDTO updatedUser = travelService.save(travelDTO);

        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    /**
     * GET /users : Updates an existing Travel.
     *
     * @param login the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel
     * @return the ResponseEntity with status 409 (CONFLICT) if the login is already in use
     */
    @GetMapping("/travels/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<TravelDTO> getTravel(@PathVariable String login) {
        log.debug("REST request to update Travel : {}", login);
        TravelDTO existingUser = travelService.findOneByLogin(login.toLowerCase());

        return existingUser != null ? ResponseEntity.ok(existingUser) : ResponseEntity.notFound().build();
    }

    /**
     * DELETE /travels/:login : delete the "login" Travel.
     *
     * @param login the login of the travel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/travels/delete/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete Travel: {}", login);
        travelService.deleteByLogin(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A travel is deleted with identifier " + login, login)).build();
    }
}
