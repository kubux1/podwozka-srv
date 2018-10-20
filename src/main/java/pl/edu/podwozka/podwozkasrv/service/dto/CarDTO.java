package pl.edu.podwozka.podwozkasrv.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.edu.podwozka.podwozkasrv.config.Constants;
import pl.edu.podwozka.podwozkasrv.domain.Car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CarDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String driverLogin;

    @Size(max = 50)
    private String brand;

    @Size(max = 50)
    private String model;

    @Size(max = 50)
    private String color;

    private Long productionYear;

    private Long registrationNumber;

    private Long maxPassengersCapacity;

    public CarDTO() {
        // Empty constructor needed for Jackson.
    }

    public CarDTO(Car car) {
        this.id = car.getId();
        this.driverLogin = car.getDriverLogin();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.color = car.getColor();
        this.productionYear = car.getProductionYear();
        this.registrationNumber = car.getRegistrationNumber();
        this.maxPassengersCapacity = car.getMaxPassengersCapacity();
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

