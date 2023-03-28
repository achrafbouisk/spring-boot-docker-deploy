package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.infrastructure.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody String newPassword){
        return userService.updatePassword(id, newPassword);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        return userService.resetPassword(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable String id, @RequestBody User user) {
        return userService.updateUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }

}

