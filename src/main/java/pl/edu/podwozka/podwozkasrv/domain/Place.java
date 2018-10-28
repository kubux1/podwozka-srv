package pl.edu.podwozka.podwozkasrv.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "place")
public class Place extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -7062814821221097048L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;

    private double longitude;

    private String name;

    @JoinColumn(name = "address_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

    @Override
    public boolean equals(Object o) {
        return DomainConstants.getEqualsMethodWithoutMetadataColumns(this, o);
    }

    @Override
    public int hashCode() {
        return DomainConstants.getHashCodeMethodWithoutMetadataColumns(this);
    }
}
