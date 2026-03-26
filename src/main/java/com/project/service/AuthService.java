package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.exception.UserNotFoundException;
import com.project.exception.InvalidCredentialsException;
import com.project.dto.LoginRequest;
import com.project.dto.LoginResponse;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Authenticate user with email and password
     */
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        // Check if user exists
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        
        User user = userOptional.get();
        
        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password for user: " + email);
        }
        
        // Generate token (simple approach - in production use JWT)
        String token = UUID.randomUUID().toString();
        
        return new LoginResponse(true, "Login successful", token, user.getId());
    }
    
    /**
     * Register a new user
     */
    public User registerUser(String name, String email, String password, String role) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists with email: " + email);
        }
        
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "USER");
        
        return userRepository.save(user);
    }
    
    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
