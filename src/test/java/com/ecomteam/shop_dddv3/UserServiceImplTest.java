package com.ecomteam.shop_dddv3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecomteam.shop_dddv3.config.authConfig.JwtService;
import com.ecomteam.shop_dddv3.domain.models.*;
import com.ecomteam.shop_dddv3.domain.payload.requests.AuthenticationRequest;
import com.ecomteam.shop_dddv3.domain.repositories.ConfirmationTokenRepository;
import com.ecomteam.shop_dddv3.domain.repositories.TokenRepository;
import com.ecomteam.shop_dddv3.domain.repositories.UserRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import com.ecomteam.shop_dddv3.infrastructure.services.mailing.MailService;
import com.ecomteam.shop_dddv3.infrastructure.services.users.UserService;
import com.ecomteam.shop_dddv3.infrastructure.services.users.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
//
//    @Mock
//    private ConfirmationTokenRepository confirmationTokenRepository;
//
//    @Mock
//    private ErrorMessage errorMessage;
//
//    @Mock
//    private MailService mailService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TokenRepository tokenRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Test
//    public void testAuthenticate_whenEmailNotVerified_thenReturnsForbidden() {
//        // arrange
//        AuthenticationRequest request = new AuthenticationRequest();
//        request.setEmail("test@test.com");
//        request.setPassword("password");
//
//        User user = new User();
//        user.setMailVerified(false);
//
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
//
//        // act
//        ResponseEntity<?> response = userService.authenticate(request);
//
//        // assert
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        assertEquals("Please verify your email", response.getBody());
//    }
//
//    @Test
//    public void testAuthenticate_whenEmailNotFound_thenReturnsNotFound() {
//        // arrange
//        AuthenticationRequest request = new AuthenticationRequest();
//        request.setEmail("test@test.com");
//        request.setPassword("password");
//
//        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
//
//        // act
//        ResponseEntity<?> response = userService.authenticate(request);
//
//        // assert
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Email not found", response.getBody());
//    }
}
