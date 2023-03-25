package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.payload.requests.AuthenticationRequest;
import com.ecomteam.shop_dddv3.domain.payload.requests.RegisterRequest;
import com.ecomteam.shop_dddv3.domain.payload.responses.AuthenticationResponse;
import com.ecomteam.shop_dddv3.domain.payload.responses.MessageResponse;
import com.ecomteam.shop_dddv3.infrastructure.services.users.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/signin")
    public AuthenticationResponse authenticateUser(@Valid @RequestBody AuthenticationRequest loginRequest) {
        return userService.authenticate(loginRequest);
    }

    @PostMapping("/signup")
    public MessageResponse registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        return userService.register(signUpRequest);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmAccount(@RequestParam("token")String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }

}
