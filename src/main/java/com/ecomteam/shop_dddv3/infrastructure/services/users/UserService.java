package com.ecomteam.shop_dddv3.infrastructure.services.users;

import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.domain.payload.requests.AuthenticationRequest;
import com.ecomteam.shop_dddv3.domain.payload.requests.RegisterRequest;
import com.ecomteam.shop_dddv3.domain.payload.responses.AuthenticationResponse;
import com.ecomteam.shop_dddv3.domain.payload.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    MessageResponse register(RegisterRequest request);

    ResponseEntity<?> confirmEmail(String confirmationToken);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(String id);

    ResponseEntity<String> updateUserById(String id, User user);

    ResponseEntity<?> updatePassword(String id, String newPassword);

    ResponseEntity<String> deleteUserById(String id);

}
