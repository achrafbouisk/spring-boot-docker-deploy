package com.ecomteam.shop_dddv3.infrastructure.services.addresses;

import com.ecomteam.shop_dddv3.domain.models.Address;
import com.ecomteam.shop_dddv3.domain.repositories.AddressRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final ErrorMessage errorMessage;

    @Override
    public ResponseEntity<?> getAllAddresses() {
        List<Address> address = addressRepository.findAll();
        if (!address.isEmpty())
            return new ResponseEntity<>(address,HttpStatus.OK);
        else{
            errorMessage.setMessage("Address Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> createAddress(Address address) {
        Address address1 = addressRepository.save(address);
        if(address1 != null)
            return new ResponseEntity<>("Address saving successfully",HttpStatus.CREATED);
        else{
            errorMessage.setMessage("Error while saving address");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateAddress(String id, Address address) {
        Optional<Address> exist = addressRepository.findById(id);
        if(exist.isPresent()){
            Address req = exist.get();
            req.setCity(address.getCity());
            req.setStreet(address.getStreet());
            req.setState(address.getState());
            req.setCountry(address.getCountry());
            req.setZipCode(address.getZipCode());
            addressRepository.save(req);
            return new ResponseEntity<>("Address has been updated successfully",HttpStatus.ACCEPTED);
        }
        else{
            errorMessage.setMessage("Error while updating address");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteAddress(String id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return new ResponseEntity<>("Address deleted successfully!", HttpStatus.OK);
        }
        else{
            errorMessage.setMessage("Address Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
