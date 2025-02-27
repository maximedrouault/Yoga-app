package com.openclassrooms.starterjwt.integrations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void authenticateUser_ValidCredentials_ReturnsJwtResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("yoga@studio.com"))
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("Admin"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void authenticateUser_NonAdminUser_ReturnsJwtResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");

        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    void authenticateUser_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void registerUser_ValidRequest_ReturnsSuccessMessage() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password123");

        String signupRequestJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void registerUser_EmailAlreadyTaken_ReturnsErrorMessage() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com"); // Email already taken
        signupRequest.setFirstName("Existing");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password123");

        String signupRequestJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }
}
