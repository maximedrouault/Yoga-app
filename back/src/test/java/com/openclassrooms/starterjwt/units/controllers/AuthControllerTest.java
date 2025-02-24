package com.openclassrooms.starterjwt.units.controllers;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;


    @Test
    void authenticateUser_ValidCredentials_ReturnsJwtResponse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        User user = new User("user@example.com", "LastName", "FirstName", "encodedPassword", false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals("user@example.com", jwtResponse.getUsername());
    }

    @Test
    void authenticateUser_UserIsNull_ReturnsIncompleteJwtResponse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals("user@example.com", jwtResponse.getUsername());
        assertEquals(false, jwtResponse.getAdmin());
    }

    @Test
    void registerUser_ValidSignupRequest_ReturnsSuccessMessage() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setLastName("LastName");
        signupRequest.setFirstName("FirstName");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertNotNull(messageResponse);
        assertEquals("User registered successfully!", messageResponse.getMessage());
    }

    @Test
    void registerUser_EmailAlreadyTaken_ReturnsErrorMessage() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existinguser@example.com");
        signupRequest.setLastName("LastName");
        signupRequest.setFirstName("FirstName");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("existinguser@example.com")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertNotNull(messageResponse);
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }
}
