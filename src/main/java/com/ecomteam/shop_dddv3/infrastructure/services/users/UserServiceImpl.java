package com.ecomteam.shop_dddv3.infrastructure.services.users;

import com.ecomteam.shop_dddv3.config.authConfig.JwtService;
import com.ecomteam.shop_dddv3.domain.models.*;
import com.ecomteam.shop_dddv3.domain.payload.requests.AuthenticationRequest;
import com.ecomteam.shop_dddv3.domain.payload.requests.RegisterRequest;
import com.ecomteam.shop_dddv3.domain.payload.responses.AuthenticationResponse;
import com.ecomteam.shop_dddv3.domain.repositories.ConfirmationTokenRepository;
import com.ecomteam.shop_dddv3.domain.repositories.TokenRepository;
import com.ecomteam.shop_dddv3.domain.repositories.UserRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import com.ecomteam.shop_dddv3.infrastructure.services.mailing.MailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ErrorMessage errorMessage;
    private final MailService mailService;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final HttpServletRequest request;

    @Value("${app.url}")
    private String appUrl;



    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        if(!user.isMailVerified()){
            return new ResponseEntity<>("Please verify your email", HttpStatus.FORBIDDEN);
        }
        else if (!userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return ResponseEntity.ok(new AuthenticationResponse(
                jwtToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        ));
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.FORBIDDEN);
        }
        // Create new user's account
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/api/v2/auth/confirm-account?token="+confirmationToken.getConfirmationToken());
        mailService.sendMail(mailMessage);

        return new ResponseEntity<>("Please check your email to verify it.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUserEntity().getEmail());
            user.setMailVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }

    // GET ALL USERS
    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            errorMessage.setMessage("Users Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    // GET SINGLE USER
    @Override
    public ResponseEntity<?> getUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else {
            errorMessage.setMessage("User Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE USER
    @Override
    public ResponseEntity<String> updateUserById(String id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User UserReq = existingUser.get();
            UserReq.setUsername(user.getUsername());
            UserReq.setEmail(user.getEmail());
            UserReq.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(UserReq);
            return new ResponseEntity<>("User has been updated successfully", HttpStatus.ACCEPTED);
        } else {
            errorMessage.setMessage("User Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> updatePassword(String id, String newPassword) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User UserReq = user.get();
            UserReq.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(UserReq);
            return new ResponseEntity<>("Password has been updated successfully", HttpStatus.ACCEPTED);
        } else {
            errorMessage.setMessage("User Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow();

        if(user == null) return new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND);

        var resetToken = user.getResetPasswordToken();
        userRepository.save(user);

        String resetPasswordUrl = appUrl + "/reset-password?token=" + resetToken;


        String message = "Please click on this link to reset your password: " + resetPasswordUrl;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Password Reset Request");
        mailMessage.setText(message);
        mailService.sendMail(mailMessage);

        return new ResponseEntity<>("Please check your email to reset the password.", HttpStatus.OK);

    }

    //DELETE USER
    @Override
    public ResponseEntity<String> deleteUserById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
        } else {
            errorMessage.setMessage("User Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

}
