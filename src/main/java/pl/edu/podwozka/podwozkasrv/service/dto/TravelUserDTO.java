package pl.edu.podwozka.podwozkasrv.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.podwozka.podwozkasrv.domain.DomainConstants;
import pl.edu.podwozka.podwozkasrv.domain.TravelUser;

@Getter
@Setter
@ToString
public class TravelUserDTO {

    private Long id;

    private Long travelId;

    private String userLogin;

    private boolean userAccepted;

    private String startPlace;

    private String endPlace;

    private String pickUpDateTime;

    public TravelUserDTO(TravelUser travelUser) {
        this.travelId = travelUser.getTravelId();
        this.userLogin = travelUser.getUserLogin();
        this.userAccepted = travelUser.isUserAccepted();
        this.startPlace = travelUser.getStartPlace();
        this.endPlace = travelUser.getEndPlace();
        this.pickUpDateTime = travelUser.getPickUpDateTime();
    }

    public TravelUserDTO() {
        // Empty constructor needed for Jackson.
    }

    @Override
    public boolean equals(Object o) {
        return DomainConstants.getEqualsMethodWithoutMetadataColumns(this, o);
    }

    @Override
    public int hashCode() {
        return DomainConstants.getHashCodeMethodWithoutMetadataColumns(this);
    }
}
