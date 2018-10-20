package pl.edu.podwozka.podwozkasrv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.podwozka.podwozkasrv.config.Constants;
import pl.edu.podwozka.podwozkasrv.domain.Place;
import pl.edu.podwozka.podwozkasrv.repository.PlaceRepository;
import pl.edu.podwozka.podwozkasrv.service.dto.PlaceDTO;
import pl.edu.podwozka.podwozkasrv.service.mapper.PlaceMapper;

import java.util.Optional;

@Service
public class PlaceService {

    private final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    private final PlaceMapper mapper;

    private final PlaceRepository repository;

    public PlaceService(PlaceMapper placeMapper, PlaceRepository repository) {
        this.mapper = placeMapper;
        this.repository = repository;
    }

    public Place createPlace(PlaceDTO placeDTO) {
        Place place = mapper.placeDTOToPlace(placeDTO);
        repository.save(place);

        LOGGER.debug("Created Information for Place: {}", place);
        return place;
    }

    public Optional<PlaceDTO> updatePlace(PlaceDTO placeDTO) {
        return Optional.of(repository
                .findOneById(placeDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(place -> {
                    place = mapper.placeDTOToPlace(placeDTO);
                    LOGGER.debug("Changed Information for Place: {}", place);
                    return place;
                })
                .map(PlaceDTO::new);
    }

    public void deletePlace(Long id) {
        repository.findOneById(id).ifPresent(place -> {
            repository.delete(place);
            LOGGER.debug("Deleted Place: {}", place);
        });
    }

    @Transactional(readOnly = true)
    public Page<PlaceDTO> getAllPlaces(Pageable pageable) {
        return repository.findAll(pageable, Constants.ANONYMOUS_USER).map(PlaceDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<Place> getPlace(Long id) {
        return repository.findOneById(id);
    }
}
