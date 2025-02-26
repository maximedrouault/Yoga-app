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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUtil authUtil;

    private final HttpHeaders adminHttpHeaders = new HttpHeaders();

    @BeforeEach
    void setUp() throws Exception {
        adminHttpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authUtil.obtainAdminJwtToken());
    }


    @Test
    void findById_TeacherExists_ReturnsTeacher() throws Exception {
        long teacherId = 1L;

        mockMvc.perform(get("/api/teacher/{id}", teacherId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(1),
                        jsonPath("$.firstName").value("Margot"),
                        jsonPath("$.lastName").value("DELAHAYE"),
                        jsonPath("$.createdAt").value("2023-01-01T00:00:00"),
                        jsonPath("$.updatedAt").value("2023-02-15T12:30:00")
                );
    }

    @Test
    void findById_TeacherNotFound_ReturnsNotFound() throws Exception {
        long TeacherId = 999; // Teacher with id 999 does not exist

        mockMvc.perform(get("/api/teacher/{id}", TeacherId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() throws Exception {
        String invalidId = "invalid-id";

        mockMvc.perform(get("/api/teacher/{id}", invalidId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_TeachersExists_ReturnsListOfTeachers() throws Exception {
        mockMvc.perform(get("/api/teacher")
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpectAll(
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].lastName").value("DELAHAYE"),
                        jsonPath("$[0].createdAt").value("2023-01-01T00:00:00"),
                        jsonPath("$[0].updatedAt").value("2023-02-15T12:30:00")
                )
                .andExpectAll(
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].lastName").value("THIERCELIN"),
                        jsonPath("$[1].createdAt").value("2023-03-01T00:00:00"),
                        jsonPath("$[1].updatedAt").value("2023-04-15T12:30:00")
                );
    }
}
