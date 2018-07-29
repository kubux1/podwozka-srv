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
import pl.edu.podwozka.podwozkasrv.web.rest.util.HeaderUtil;
import pl.edu.podwozka.podwozkasrv.web.rest.util.PaginationUtil;

import static org.springframework.http.HttpStatus.CONFLICT;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
     * @return the ResponseEntity with status 409 (CONFLICT) if the travel has an id
     */
    @PostMapping("/travels")
    public ResponseEntity<TravelDTO> createTravel(@Valid @RequestBody TravelDTO travelDTO) throws URISyntaxException {
        log.debug("REST request to save Travel : {}", travelDTO);

        if (travelDTO.getId() != null) {
            return ResponseEntity.status(CONFLICT).build();
        } else {
            TravelDTO newTravel = travelService.save(travelDTO);
            return ResponseEntity.created(new URI("/api/travels/" + newTravel.getId()))
                    .headers(HeaderUtil.createAlert( "A travel is created with identifier ",
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
        TravelDTO updatedTravel = travelService.save(travelDTO);

        return updatedTravel != null ? ResponseEntity.ok(updatedTravel) : ResponseEntity.notFound().build();
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
     * GET /travels : Updates an existing Travel.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/travels")
    public ResponseEntity<List<TravelDTO>> getTravel(Pageable pageable, @RequestParam(required = true) String login) {
        log.debug("REST request to update Travel : {}", login);
        Page<TravelDTO> page = travelService.findAllByLogin(pageable, login);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                String.format("/api/travels?login=%b", login));
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
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A travel is deleted with identifier ",
                id.toString())).build();
    }
}
