package pl.edu.podwozka.podwozkasrv.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.edu.podwozka.podwozkasrv.domain.Address;

@Getter
@Setter
@ToString
public class AddressDTO {

    private Long id;

    private Long buildingNumber;

    private String street;

    private String postcode;

    private String locality;

    private String country;

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.buildingNumber = address.getBuildingNumber();
        this.street = address.getStreet();
        this.postcode = address.getPostcode();
        this.locality = address.getLocality();
        this.country = address.getCountry();
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
