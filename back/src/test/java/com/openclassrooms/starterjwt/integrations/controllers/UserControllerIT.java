package com.openclassrooms.starterjwt.integrations.controllers;

import com.openclassrooms.starterjwt.utils.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUtil authUtil;

    private String adminJwtToken;
    private String nonAdminJwtToken;

    @BeforeEach
    void setUp() throws Exception {
        this.adminJwtToken = authUtil.obtainAdminJwtToken();
        this.nonAdminJwtToken = authUtil.obtainNonAdminJwtToken();
    }


    @Test
    void findById_UserExists_ReturnsUser() throws Exception {
        long userId = 2L;

        mockMvc.perform(get("/api/user/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(2),
                        jsonPath("$.email").value("john.doe@example.com"),
                        jsonPath("$.lastName").value("Doe"),
                        jsonPath("$.firstName").value("John"),
                        jsonPath("$.admin").value(false),
                        jsonPath("$.password").doesNotExist(),
                        jsonPath("$.createdAt").value("2023-01-01T00:00:00"),
                        jsonPath("$.updatedAt").value("2023-02-15T12:30:00")
                );
    }

    @Test
    void findById_UserNotFound_ReturnsNotFound() throws Exception {
        long userId = 999; // User with id 999 does not exist

        mockMvc.perform(get("/api/user/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() throws Exception {
        String invalidId = "invalid-id";

        mockMvc.perform(get("/api/user/{id}", invalidId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deleteUser_UserExists_ReturnsOk() throws Exception {
        long userId = 2L;

        mockMvc.perform(delete("/api/user/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + nonAdminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_UnauthorizedUser_ReturnsUnauthorized() throws Exception {
        long userId = 2L; // ID of other user than the authenticated user

        mockMvc.perform(delete("/api/user/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUser_UserNotFound_ReturnsNotFound() throws Exception {
        long userId = 999; // User with id 999 does not exist

        mockMvc.perform(delete("/api/user/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_InvalidId_ReturnsBadRequest() throws Exception {
        String invalidId = "invalid-id";

        mockMvc.perform(delete("/api/user/{id}", invalidId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
