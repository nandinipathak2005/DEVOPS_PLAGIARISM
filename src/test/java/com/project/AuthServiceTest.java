package com.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.AuthService;
import com.project.dto.LoginRequest;
import com.project.dto.LoginResponse;
import com.project.exception.UserNotFoundException;
import com.project.exception.InvalidCredentialsException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private AuthService authService;
    
    private BCryptPasswordEncoder passwordEncoder;
    private User testUser;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        
        // Inject passwordEncoder using ReflectionTestUtils
        ReflectionTestUtils.setField(authService, "passwordEncoder", passwordEncoder);
        
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole("USER");
    }
    
    /**
     * Test Case 1: Login with valid credentials should succeed
     */
    @Test
    public void testLoginSuccessful() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        
        // Act
        LoginResponse response = authService.login(loginRequest);
        
        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getToken());
        assertEquals(1L, response.getUserId());
        
        // Verify repository was called
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
    
    /**
     * Test Case 2: Login with wrong password should fail
     */
    @Test
    public void testLoginFailureInvalidPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(loginRequest);
        });
        
        // Verify repository was called
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
    
    /**
     * Test Case 3: Login with non-existent user should fail
     */
    @Test
    public void testLoginFailureUserNotFound() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "password123");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        
        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.login(loginRequest);
        });
        
        assertEquals("User not found with email: nonexistent@example.com", exception.getMessage());
        
        // Verify repository was called
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
    
    /**
     * Test Case 4: Register new user should succeed
     */
    @Test
    public void testRegisterUserSuccessful() {
        // Arrange
        String name = "New User";
        String email = "newuser@example.com";
        String password = "securepassword123";
        String role = "USER";
        
        User newUser = new User();
        newUser.setId(2L);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        
        // Act
        User registeredUser = authService.registerUser(name, email, password, role);
        
        // Assert
        assertNotNull(registeredUser);
        assertEquals(2L, registeredUser.getId());
        assertEquals(name, registeredUser.getName());
        assertEquals(email, registeredUser.getEmail());
        assertEquals(role, registeredUser.getRole());
        
        // Verify repository methods were called
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    /**
     * Test Case 5: Register with duplicate email should fail
     */
    @Test
    public void testRegisterUserDuplicateEmailFailure() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.registerUser("Another User", email, "password456", "USER");
        });
        
        // Verify findByEmail was called but save was not
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
    
    /**
     * Test Case 6: Register user with default role should set role to USER
     */
    @Test
    public void testRegisterUserWithDefaultRole() {
        // Arrange
        String name = "Default Role User";
        String email = "defaultrole@example.com";
        String password = "password789";
        
        User registeredUser = new User();
        registeredUser.setId(3L);
        registeredUser.setName(name);
        registeredUser.setEmail(email);
        registeredUser.setPassword(passwordEncoder.encode(password));
        registeredUser.setRole("USER");
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(registeredUser);
        
        // Act
        User user = authService.registerUser(name, email, password, null);
        
        // Assert
        assertNotNull(user);
        assertEquals("USER", user.getRole());
        assertEquals(email, user.getEmail());
        
        // Verify repository methods were called
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
