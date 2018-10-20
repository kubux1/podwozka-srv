package pl.edu.podwozka.podwozkasrv.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.edu.podwozka.podwozkasrv.domain.Place;

@Getter
@Setter
@ToString
public class PlaceDTO {

    private Long id;

    private double latitude;

    private double longitude;

    private String name;

    private AddressDTO address;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.name = place.getName();
        this.address = new AddressDTO(place.getAddress());
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
