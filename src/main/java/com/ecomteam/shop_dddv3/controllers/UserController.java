package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.infrastructure.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2/users")
public class UserController {
    
    @Autowired
    private UserService usererService;

	// @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return usererService.getAllUsers();
    }

	// @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        return usererService.getUserById(id);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody String newPassword){
        return usererService.updatePassword(id, newPassword);
    }

	// @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable String id, @RequestBody User user) {
        return usererService.updateUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id) {
        return usererService.deleteUserById(id);
    }

}

