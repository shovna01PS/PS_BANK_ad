package com.sapient.PSBank.controller;

import com.sapient.PSBank.dto.JwtRequest;
import com.sapient.PSBank.dto.JwtResponse;
import com.sapient.PSBank.jwt.JwtAuthenticationHelper;
import com.sapient.PSBank.repository.CustomerRepository;
import com.sapient.PSBank.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
        return new ResponseEntity<>(authService.login(jwtRequest), HttpStatus.OK);
    }
}
