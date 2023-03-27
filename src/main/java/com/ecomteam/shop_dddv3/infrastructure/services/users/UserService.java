package com.ecomteam.shop_dddv3.infrastructure.services.users;

import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.domain.payload.requests.AuthenticationRequest;
import com.ecomteam.shop_dddv3.domain.payload.requests.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> authenticate(AuthenticationRequest request);

    ResponseEntity<?> register(RegisterRequest request);

    ResponseEntity<?> confirmEmail(String confirmationToken);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(String id);

    ResponseEntity<String> updateUserById(String id, User user);

    ResponseEntity<?> updatePassword(String id, String newPassword);

    ResponseEntity<String> deleteUserById(String id);

}
