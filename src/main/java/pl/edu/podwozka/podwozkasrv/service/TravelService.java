package pl.edu.podwozka.podwozkasrv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import pl.edu.podwozka.podwozkasrv.domain.TravelUser;
import pl.edu.podwozka.podwozkasrv.repository.TravelRepository;
import pl.edu.podwozka.podwozkasrv.repository.TravelUserRepository;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelUserDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.TravelMapper;

import java.time.Instant;
import java.util.Set;
import java.util.HashSet;

import pl.edu.podwozka.podwozkasrv.domain.User;
import pl.edu.podwozka.podwozkasrv.repository.UserRepository;
import pl.edu.podwozka.podwozkasrv.service.mapper.TravelUserMapper;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class TravelService {

    private final Logger log = LoggerFactory.getLogger(TravelService.class);

    private final TravelRepository travelRepository;

    private final UserRepository userRepository;

    private final TravelMapper travelMapper;

    private final TravelUserMapper travelUserMapper;

    private final TravelUserRepository travelUserRepository;

    public TravelService(TravelRepository travelRepository, TravelMapper travelMapper, UserRepository userRepository,
                         TravelUserRepository travelUserRepository, TravelUserMapper travelUserMapper) {
        this.travelRepository = travelRepository;
        this.userRepository = userRepository;
        this.travelMapper = travelMapper;
        this.travelUserRepository = travelUserRepository;
        this.travelUserMapper = travelUserMapper;
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
        return travelRepository.findAllByDriverLogin(pageable, login).map(TravelDTO::new);
    }

    /**
     * Get all the past travels by login.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findAllPastByLogin(Pageable pageable, String login, Instant time) {
        log.debug("Request to get all travels");
        return travelRepository.findAllByDriverLoginAndPickUpDatetimeBefore(pageable, login,
                time).map(TravelDTO::new);
    }

    /**
     * Get all the coming travels by login.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findAllComingByLogin(Pageable pageable, String login, Instant time) {
        log.debug("Request to get all travels");
        return travelRepository.findAllByDriverLoginAndPickUpDatetimeAfter(pageable, login,
                time).map(TravelDTO::new);
    }

    /**
     * Get all the past travels by login.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findAllForPassengerPastByLogin(Pageable pageable, String login, Instant time) {
        log.debug("Request to get all travels");
        return travelRepository.findAllWithPassengersByPassengersLoginAndPickUpDatetimeBefore(pageable, login,
                time).map(TravelDTO::new);
    }

    /**
     * Get all the coming travels by login.
     *
     * @param pageable the pagination information
     * @param login of the owner
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findAllForPassengerComingByLogin(Pageable pageable, String login, Instant time) {
        log.debug("Request to get all travels");
        return travelRepository.findAllWithPassengersByPassengersLoginAndPickUpDatetimeAfter(pageable, login,
                time).map(TravelDTO::new);
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

    /**
     * Get all the travels matching passanger criterion.
     *
     * @param pageable the pagination information
     * @param travelDTO the entity to match with
     * @return the list of entities
     */
    public Page<TravelDTO> findTravelsForPassenger(Pageable pageable, TravelDTO travelDTO) {
        log.debug("Request to find travels matching passenger criterion");
        Travel travel = travelMapper.travelDTOToTravel(travelDTO);
        return travelRepository.match(
                pageable,
                travel.getStartPlace().getLatitude(),
                travel.getStartPlace().getLongitude(),
                travel.getEndPlace().getLatitude(),
                travel.getEndPlace().getLongitude(),
                travel.getPickUpDatetime()).map(TravelDTO::new);
    }

    /**
     * Sign up passenger for a travel.
     *
     * @param travelId of the travel
     * @return the persisted entity
     */
    public boolean signUp(String passengerLogin, Long travelId) {
        log.debug("Request to sign up for a Travel : {}", travelId);
        try {
            Travel travel = travelRepository.findOneById(travelId);
            Set<User> passengers = new HashSet<>();
            userRepository.findOneByLogin(passengerLogin).ifPresent(user -> {
                passengers.add(user);
            });
            for (User passenger : travel.getPassengers()) {
                // to don't remove another passengers from travel
                passengers.add(passenger);
            }
            travel.setPassengers(passengers);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkIfUserSignedForTheSameTrip(String passengerLogin, Long travelId) {
        Travel travel = travelRepository.findOneWithPassengersByPassengersLoginAndId(passengerLogin, travelId);
        if (travel == null) {
            return false;
        }
            return true;
    }

    /**
     * Get all the travels for which passenger signed up.
     *
     * @param pageable the pagination information
     * @param login of the passenger
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> passengerFindAllByLogin(Pageable pageable, String login) {
        log.debug("Request to find signed up travels by Passenger : {}", login);

        return travelRepository.findAllWithPassengersByPassengersLogin(pageable, login).map(TravelDTO::new);
    }

    /**
     * Get all the travels for which passanger signed up.
     *
     * @param pageable the pagination information
     * @param travelId travel id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelUserDTO> findAcceptanceByTravelId(Pageable pageable, Long travelId) {
        log.debug("Request to find signed up travels by id : {}", travelId);

        return travelUserRepository.findAllByTravelId(pageable, travelId).map(TravelUserDTO::new);
    }

    /**
     * Get all the travels for which passanger signed up.
     *
     * @param pageable the pagination information
     * @param login Passenger's login
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TravelUserDTO> findAcceptanceByPassenger(Pageable pageable, String login) {
        log.debug("Request to find signed up travels by id : {}", login);

        return travelUserRepository.findAllByUserLogin(pageable, login).map(TravelUserDTO::new);
    }

    /**
     * Save a acceptance.
     *
     * @param travelId travel ID
     * @param login passenger login
     * @return the persisted entity
     */
    public TravelUserDTO changeAcceptanceStatusByTravelIdAndLogin(Long travelId, String login, boolean userAccepted) {
        TravelUser travelUser = travelUserRepository.findFirstByTravelIdAndUserLogin(travelId, login);
        if (travelUser != null) {
            travelUser.setUserAccepted(userAccepted);
            return new TravelUserDTO(travelUserRepository.save(travelUser));
        }
        return null;
    }


    /**
     * Find a acceptance.
     *
     * @param travelId travel ID
     * @param login passenger login
     * @return the persisted entity
     */
    public TravelUserDTO findOneAcceptance(Long travelId, String login) {
        TravelUser travelUser = travelUserRepository.findFirstByTravelIdAndUserLogin(travelId, login);

        return (travelUser != null) ?
                new TravelUserDTO(travelUserRepository.findFirstByTravelIdAndUserLogin(travelId, login)) : null;
    }

    /**
     * Add other fields in a travel user table.
     *
     * @param travelId travel ID
     * @param passengerLogin passenger login
     * @param startPlace passenger start place
     * @param endPlace passenger end place
     * @param pickUpDateTime passenger pick up date and time
     * @return true or false
     */
    public boolean addFields(String passengerLogin, Long travelId, String startPlace, String endPlace,
                             String pickUpDateTime) {
        TravelUser travelUser = travelUserRepository.findFirstByTravelIdAndUserLogin(travelId, passengerLogin);
        if (travelUser != null) {
            travelUser.setStartPlace(startPlace);
            travelUser.setEndPlace(endPlace);
            travelUser.setPickUpDateTime(pickUpDateTime);
            return true;
        }
        return false;
    }
}
