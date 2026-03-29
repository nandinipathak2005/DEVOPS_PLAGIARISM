package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.service.AuthService;
import com.project.model.User;
import com.project.dto.LoginRequest;
import com.project.dto.LoginResponse;
import com.project.dto.RegisterRequest;
import com.project.exception.UserNotFoundException;
import com.project.exception.InvalidCredentialsException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Login endpoint - authenticate user with email and password
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            LoginResponse errorResponse = new LoginResponse(false, e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        } catch (InvalidCredentialsException e) {
            LoginResponse errorResponse = new LoginResponse(false, e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
    
    /**
     * Register endpoint - create a new user account
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.registerUser(
                registerRequest.getName(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                "USER"  // Default role
            );
            
            LoginResponse response = new LoginResponse(
                true,
                "Registration successful",
                null,
                user.getId()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse(false, e.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        }
    }
    
    /**
     * Health check endpoint to verify auth service is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is running");
    }
}
