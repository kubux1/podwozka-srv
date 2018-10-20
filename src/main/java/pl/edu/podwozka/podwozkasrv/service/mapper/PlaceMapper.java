package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.Address;
import pl.edu.podwozka.podwozkasrv.domain.Place;
import pl.edu.podwozka.podwozkasrv.service.dto.PlaceDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PlaceMapper {

    @Autowired
    private AddressMapper addressMapper;

    public PlaceDTO placeToPlaceDTO(Place place) {
        return new PlaceDTO(place);
    }

    public List<PlaceDTO> placesToPlaceDTOs(List<Place> places) {
        return places.stream()
                .filter(Objects::nonNull)
                .map(this::placeToPlaceDTO)
                .collect(Collectors.toList());
    }

    public Place placeDTOToPlace(PlaceDTO placeDTO) {
        if (placeDTO == null) {
            return null;
        } else {
            Place place = new Place();
            place.setId(placeDTO.getId());
            place.setLatitude(placeDTO.getLatitude());
            place.setLongitude(placeDTO.getLongitude());
            place.setName(placeDTO.getName());
            place.setAddress(addressMapper.addressDTOToAddress(placeDTO.getAddress()));
            return place;
        }
    }

    public List<Place> placeDTOsToPlaces(List<PlaceDTO> placeDTOs) {
        return placeDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::placeDTOToPlace)
                .collect(Collectors.toList());
    }

    public Place placeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Place place = new Place();
        place.setId(id);
        return place;
    }
}
