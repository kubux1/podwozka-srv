package pl.edu.podwozka.podwozkasrv.service.mapper;

import org.springframework.stereotype.Service;
import pl.edu.podwozka.podwozkasrv.domain.Address;
import pl.edu.podwozka.podwozkasrv.service.dto.AddressDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AddressMapper {

    public AddressDTO addressToAddressDTO(Address address) {
        return new AddressDTO(address);
    }

    public List<AddressDTO> addressesToAddressDTOs(List<Address> addresses) {
        return addresses.stream()
                .filter(Objects::nonNull)
                .map(this::addressToAddressDTO)
                .collect(Collectors.toList());
    }

    public Address addressDTOToAddress(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        } else {
            Address address = new Address();
            address.setId(addressDTO.getId());
            address.setBuildingNumber(addressDTO.getBuildingNumber());
            address.setStreet(addressDTO.getStreet());
            address.setPostcode(addressDTO.getPostcode());
            address.setLocality(addressDTO.getLocality());
            address.setCountry(addressDTO.getCountry());
            return address;
        }
    }

    public List<Address> addressDTOsToAddresses(List<AddressDTO> addressDTOs) {
        return addressDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::addressDTOToAddress)
                .collect(Collectors.toList());
    }

    public Address addressFromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
