package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.TravelUser;
import pl.edu.podwozka.podwozkasrv.service.dto.TravelUserDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TravelUserMapper {

    public TravelUserDTO userToUserDTO(TravelUser travelUser) {
        return new TravelUserDTO(travelUser);
    }

    public List<TravelUserDTO> travelUsersToTravelUserDTOs(List<TravelUser> users) {
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    public TravelUser travelUserDTOToTravelUser(TravelUserDTO travelUserDTO) {
        if (travelUserDTO == null) {
            return null;
        } else {
            TravelUser travelUser = new TravelUser();
            travelUser.setId(travelUserDTO.getId());
            travelUser.setTravelId(travelUserDTO.getTravelId());
            travelUser.setUserLogin(travelUserDTO.getUserLogin());
            travelUser.setUserAccepted(travelUserDTO.isUserAccepted());
            return travelUser;
        }
    }

    public List<TravelUser> travelUserDTOsTotravelUsers(List<TravelUserDTO> userDTOs) {
        return userDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::travelUserDTOToTravelUser)
                .collect(Collectors.toList());
    }

}

