package com.clubatletismolosangeles.losangeleswebback.controller;

import com.clubatletismolosangeles.losangeleswebback.dto.LoginRequest;
import com.clubatletismolosangeles.losangeleswebback.dto.LoginResponse;
import com.clubatletismolosangeles.losangeleswebback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }
}