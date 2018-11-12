package pl.edu.podwozka.podwozkasrv.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "pd_travel_user")
public class TravelUser extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -7062813451221097048L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "travel_id")
    private Long travelId;

    @Size(max = 50)
    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_accepted")
    private boolean userAccepted;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
