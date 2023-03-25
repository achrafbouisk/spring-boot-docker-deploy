package com.ecomteam.shop_dddv3.infrastructure.services.addresses;

import com.ecomteam.shop_dddv3.domain.models.Address;
import org.springframework.http.ResponseEntity;

public interface AddressService {
    
    ResponseEntity<?> getAllAddresses();

    ResponseEntity<String> createAddress(Address address);

    ResponseEntity<String> updateAddress(String id, Address address);

    ResponseEntity<String> deleteAddress(String id);
}
