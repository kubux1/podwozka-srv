package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.Travel;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelDTO;

import java.util.*;
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

    public Travel travelDTOToTravel(TravelDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            Travel travel = new Travel();
            travel.setId(userDTO.getId());
            travel.setStartPlace(userDTO.getStartPlace());
            travel.setEndPlace(userDTO.getEndPlace());
            travel.setLogin(userDTO.getLogin());
            travel.setFirstName(userDTO.getFirstName());
            travel.setLastName(userDTO.getLastName());
            return travel;
        }
    }

    public List<Travel> travelDTOsToTravels(List<TravelDTO> userDTOs) {
        return userDTOs.stream()
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
