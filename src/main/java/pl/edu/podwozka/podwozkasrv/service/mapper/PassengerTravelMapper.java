package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.PassengerTravel;
import pl.edu.podwozka.podwozkasrv.service.dto.PassengerTravelDTO;
import pl.edu.podwozka.podwozkasrv.time.TimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for the entity PassengerTravel and its DTO called PassengerTravelDTO.
 * <p>
 * TODO: Consider MapStruct
 */
@Service
public class PassengerTravelMapper {

    public PassengerTravelDTO travelToPassengerTravelDTO(PassengerTravel travel) {
        return new PassengerTravelDTO(travel);
    }

    public List<PassengerTravelDTO> travelsToPassengerTravelDTOs(List<PassengerTravel> travels) {
        return travels.stream()
                .filter(Objects::nonNull)
                .map(this::travelToPassengerTravelDTO)
                .collect(Collectors.toList());
    }

    public PassengerTravel travelDTOToPassengerTravel(PassengerTravelDTO travelDTO) {
        if (travelDTO == null) {
            return null;
        } else {
            PassengerTravel travel = new PassengerTravel();
            travel.setId(travelDTO.getId());
            travel.setStartPlace(travelDTO.getStartPlace());
            travel.setEndPlace(travelDTO.getEndPlace());
            travel.setLogin(travelDTO.getLogin());
            travel.setFirstName(travelDTO.getFirstName());
            travel.setLastName(travelDTO.getLastName());
            travel.setDriverId(travelDTO.getDriverId());
            travel.setPickUpDatetime(TimeUtil.localDateTimeToInstant(travelDTO.getPickUpDatetime()));
            return travel;
        }
    }

    public List<PassengerTravel> passengerTravelDTOsToPassengerTravels(List<PassengerTravelDTO> travelDTOs) {
        return travelDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::travelDTOToPassengerTravel)
                .collect(Collectors.toList());
    }

    public PassengerTravel travelFromId(Long id) {
        if (id == null) {
            return null;
        }
        PassengerTravel travel = new PassengerTravel();
        travel.setId(id);
        return travel;
    }
}
