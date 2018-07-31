package pl.edu.podwozka.podwozkasrv.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import pl.edu.podwozka.podwozkasrv.repository.TravelRepository;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.TravelMapper;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class TravelService {

    private final Logger log = LoggerFactory.getLogger(TravelService.class);

    private final TravelRepository travelRepository;

    private final TravelMapper travelMapper;

    public TravelService(TravelRepository travelRepository, TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.travelMapper = travelMapper;
    }


    /**
     * Save a travel.
     *
     * @param travelDTO the entity to save
     * @return the persisted entity
     */
    public TravelDTO save(TravelDTO travelDTO) {
        Travel travel = travelMapper.travelDTOToTravel(travelDTO);
        log.debug("Request to save Travel : {}", travel);
        return new TravelDTO(travelRepository.save(travel));
    }

    /**
     * Get all the travels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all travels");
        return travelRepository.findAll(pageable).map(TravelDTO::new);
    }

    /**
     * Get all the travels by login.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findAllByLogin(Pageable pageable, String login) {
        log.debug("Request to get all travels");
        return travelRepository.findAllByLogin(pageable, login).map(TravelDTO::new);
    }

    /**
     * Get one travel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TravelDTO findOne(Long id) {
        log.debug("Request to get Travel : {}", id);
        Travel travel = travelRepository.findOneById(id);

        return (travel != null) ? new TravelDTO(travelRepository.findOneById(id)) : null;
    }

    /**
     * Delete the travel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Travel : {}", id);
        travelRepository.deleteOneById(id);
    }
}
