package pl.edu.podwozka.podwozkasrv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.domain.PassengerTravel;
import pl.edu.podwozka.podwozkasrv.repository.PassengerTravelRepository;
import pl.edu.podwozka.podwozkasrv.service.dto.PassengerTravelDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.PassengerTravelMapper;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class PassengerTravelService {

    private final Logger log = LoggerFactory.getLogger(PassengerTravelService.class);

    private final PassengerTravelRepository travelRepository;

    private final PassengerTravelMapper travelMapper;

    public PassengerTravelService(PassengerTravelRepository travelRepository, PassengerTravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }


    /**
     * Save a travel.
     *
     * @param travelDTO the entity to save
     * @return the persisted entity
     */
    public PassengerTravelDTO save(PassengerTravelDTO travelDTO) {
        PassengerTravel travel = travelMapper.travelDTOToPassengerTravel(travelDTO);
        log.debug("Request to save PassengerTravel : {}", travel);
        return new PassengerTravelDTO(travelRepository.save(travel));
    }

    /**
     * Get all the travels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PassengerTravelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all travels");
        return travelRepository.findAll(pageable).map(PassengerTravelDTO::new);
    }

    /**
     * Get all the travels by login.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PassengerTravelDTO> findAllByLogin(Pageable pageable, String login) {
        log.debug("Request to get all travels");
        return travelRepository.findAllByLogin(pageable, login).map(PassengerTravelDTO::new);
    }

    /**
     * Get one travel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PassengerTravelDTO findOne(Long id) {
        log.debug("Request to get PassengerTravel : {}", id);
        PassengerTravel travel = travelRepository.findOneById(id);

        return (travel != null) ? new PassengerTravelDTO(travel) : null;
    }

    /**
     * Delete the travel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PassengerTravel : {}", id);
        travelRepository.deleteOneById(id);
    }
}
