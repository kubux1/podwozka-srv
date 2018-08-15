package pl.edu.podwozka.podwozkasrv.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.edu.podwozka.podwozkasrv.domain.Travel;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TravelDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String login;

    @NotBlank
    private String startPlace;

    @NotBlank
    private String endPlace;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private Long passengersCount;

    private LocalDateTime pickUpDatetime;

    public TravelDTO() {} // Empty constructor needed for Jackson.

    public TravelDTO(Travel travel) {
        this.id = travel.getId();
        this.login = travel.getLogin();
        this.startPlace = travel.getStartPlace();
        this.endPlace = travel.getEndPlace();
        this.firstName = travel.getFirstName();
        this.lastName = travel.getLastName();
        this.passengersCount = travel.getPassengersCount();
        this.pickUpDatetime = travel.getPickUpDatetime().toLocalDateTime();
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
