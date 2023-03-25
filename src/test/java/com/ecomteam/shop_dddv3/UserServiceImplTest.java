package com.ecomteam.shop_dddv3;

import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.domain.repositories.UserRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import com.ecomteam.shop_dddv3.infrastructure.services.users.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ErrorMessage errorMessage;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("1", "Test User", "testuser@test.com", "password");
    }

    @Test
    public void testGetAllUsersSuccess() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<?> response = userService.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetAllUsersNotFound() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(errorMessage.getMessage()).thenReturn("Users Not Found");

        ResponseEntity<?> response = userService.getAllUsers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Users Not Found", response.getBody());
    }

    @Test
    public void testGetUserByIdSuccess() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));

        ResponseEntity<?> response = userService.getUserById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        when(errorMessage.getMessage()).thenReturn("User Not Found");

        ResponseEntity<?> response = userService.getUserById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User Not Found", response.getBody());
    }

    @Test
    public void testUpdateUserById_success() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(encoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        User updatedUser = new User(testUser.getId(), "updatedUser", "updated@example.com", "password");
        ResponseEntity<String> response = userService.updateUserById(testUser.getId(), updatedUser);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User has been updated successfully", response.getBody());
    }


    @Test
    public void testUpdateUserByIdNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        when(errorMessage.getMessage()).thenReturn("User Not Found");

        ResponseEntity<String> response = userService.updateUserById("1", testUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User Not Found", response.getBody());
    }

    @Test
    public void testDeleteUserById_success() {
        when(userRepository.existsById(testUser.getId())).thenReturn(true);
        ResponseEntity<String> response = userService.deleteUserById(testUser.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully!", response.getBody());
        verify(userRepository, times(1)).deleteById(testUser.getId());
    }

    @Test
    public void testDeleteUserById_userNotFound() {
        when(userRepository.existsById(anyString())).thenReturn(false);
        ResponseEntity<String> response = userService.deleteUserById(anyString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("User Not Found", response.getBody());
//        verify(userRepository, never()).deleteById(anyString());
    }
}
