package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;
import pl.edu.podwozka.podwozkasrv.time.TimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Travel and its DTO called TravelDTO.
 * <p>
 * TODO: Consider MapStruct
 */
@Service
public class TravelMapper {

    public TravelDTO travelToTravelDTO(Travel travel) {
        return new TravelDTO(travel);
    }

    public List<TravelDTO> travelsToTravelDTOs(List<Travel> travels) {
        return travels.stream()
                .filter(Objects::nonNull)
                .map(this::travelToTravelDTO)
                .collect(Collectors.toList());
    }

    public Travel travelDTOToTravel(TravelDTO travelDTO) {
        if (travelDTO == null) {
            return null;
        } else {
            Travel travel = new Travel();
            travel.setId(travelDTO.getId());
            travel.setStartPlace(travelDTO.getStartPlace());
            travel.setEndPlace(travelDTO.getEndPlace());
            travel.setLogin(travelDTO.getLogin());
            travel.setFirstName(travelDTO.getFirstName());
            travel.setLastName(travelDTO.getLastName());
            travel.setPassengersCount(travelDTO.getPassengersCount());
            travel.setPickUpDatetime(TimeUtil.localDateTimeToInstant(travelDTO.getPickUpDatetime()));
            return travel;
        }
    }

    public List<Travel> travelDTOsToTravels(List<TravelDTO> travelDTOs) {
        return travelDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::travelDTOToTravel)
                .collect(Collectors.toList());
    }

    public Travel travelFromId(Long id) {
        if (id == null) {
            return null;
        }
        Travel travel = new Travel();
        travel.setId(id);
        return travel;
    }
}
