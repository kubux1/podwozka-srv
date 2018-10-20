package pl.edu.podwozka.podwozkasrv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.edu.podwozka.podwozkasrv.config.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String driverLogin;

    @JsonIgnore
    @NotNull
    @Size(max = 50)
    @Column(name = "brand", length = 60, nullable = false)
    private String brand;

    @Size(max = 50)
    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @Size(max = 50)
    @Column(name = "color", length = 50, nullable = false)
    private String color;

    @Column(name = "production_year", nullable = false)
    private Long productionYear;

    @Column(name = "registration_number", nullable = false)
    private Long registrationNumber;

    @Column(name = "max_capacity", nullable = false)
    private Long maxPassangersCapacity;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
