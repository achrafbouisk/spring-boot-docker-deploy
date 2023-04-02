package com.ecomteam.shop_dddv3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v2/hello")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HelloController {

    @GetMapping
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("App is running...", HttpStatus.OK);
    }
}