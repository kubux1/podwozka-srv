package pl.edu.podwozka.podwozkasrv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@ToString
@Entity
@Table(name = "travel")
public class Travel extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -7062814821221097048L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String driverLogin;

    @NotNull
    @JoinColumn(name = "start_place", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Place startPlace;

    @NotNull
    @JoinColumn(name = "end_place", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Place endPlace;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "passengers_count")
    private Long passengersCount;

    @Column(name = "pick_up_datetime")
    private Instant pickUpDatetime;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "pd_travel_user",
            joinColumns = {@JoinColumn(name = "travel_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_login", referencedColumnName = "login")})
    @BatchSize(size = 20)
        private Set<User> passengers = new HashSet<>();
}
