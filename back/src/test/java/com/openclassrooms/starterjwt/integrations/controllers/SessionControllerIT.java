package com.openclassrooms.starterjwt.integrations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final HttpHeaders adminHttpHeaders = new HttpHeaders();

    @BeforeEach
    void setUp() throws Exception {
        adminHttpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + authUtil.obtainAdminJwtToken());
    }


    @Test
    void findById_SessionExists_ReturnsSession() throws Exception {
        long sessionId = 1L;

        mockMvc.perform(get("/api/session/{id}", sessionId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("Yoga"),
                        jsonPath("$.description").value("Yoga session"),
                        jsonPath("$.teacher_id").value(1),
                        jsonPath("$.users").isArray(),
                        jsonPath("$.users[0]").value(1),
                        jsonPath("$.date").value("2023-01-01T09:00:00.000+00:00"),
                        jsonPath("$.createdAt").value("2023-01-01T08:00:00"),
                        jsonPath("$.updatedAt").value("2023-02-15T12:30:00")
                );
    }

    @Test
    void findById_SessionNotFound_ReturnsNotFound() throws Exception {
        long sessionId = 999; // Session with id 999 does not exist

        mockMvc.perform(get("/api/session/{id}", sessionId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() throws Exception {
        String invalidId = "invalid-id";

        mockMvc.perform(get("/api/session/{id}", invalidId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_SessionsExist_ReturnsSessions() throws Exception {
        mockMvc.perform(get("/api/session")
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpectAll(
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].name").value("Yoga"),
                        jsonPath("$[0].description").value("Yoga session")
                )
                .andExpectAll(
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].name").value("Pilates"),
                        jsonPath("$[1].description").value("Pilates session")
                );
    }

    @Test
    void create_ValidSession_ReturnsCreatedSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test create session");
        sessionDto.setDate(Date.from(Instant.parse("2025-02-26T16:29:27.677Z")));
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Test create session description");
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(3),
                        jsonPath("$.name").value(sessionDto.getName()),
                        jsonPath("$.description").value(sessionDto.getDescription()),
                        jsonPath("$.teacher_id").value(sessionDto.getTeacher_id()),
                        jsonPath("$.users").isArray(),
                        jsonPath("$.users[0]").value(sessionDto.getUsers().get(0)),
                        jsonPath("$.users[1]").value(sessionDto.getUsers().get(1)),
                        jsonPath("$.date").value("2025-02-26T16:29:27.677+00:00"),
                        jsonPath("$.createdAt").isNotEmpty(),
                        jsonPath("$.updatedAt").isNotEmpty()
                );
    }

    @Test
    void create_InvalidSession_ReturnsBadRequest() throws Exception {
        SessionDto invalidSessionDto = new SessionDto();

        String invalidSessionDtoJson = objectMapper.writeValueAsString(invalidSessionDto);

        mockMvc.perform(post("/api/session")
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidSessionDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ValidSession_ReturnsUpdatedSession() throws Exception {
        long sessionId = 1L;

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated session name");
        sessionDto.setDate(Date.from(Instant.parse("2025-02-26T16:29:27.677Z")));
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Updated session description");
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        sessionDto.setCreatedAt(LocalDateTime.parse("2025-02-25T17:30:00"));
        sessionDto.setUpdatedAt(LocalDateTime.parse("2025-02-26T12:00:00"));

        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/{id}", sessionId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sessionDtoJson))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(sessionId),
                        jsonPath("$.name").value(sessionDto.getName()),
                        jsonPath("$.description").value(sessionDto.getDescription()),
                        jsonPath("$.teacher_id").value(sessionDto.getTeacher_id()),
                        jsonPath("$.users").isArray(),
                        jsonPath("$.users[0]").value(sessionDto.getUsers().get(0)),
                        jsonPath("$.users[1]").value(sessionDto.getUsers().get(1)),
                        jsonPath("$.date").value("2025-02-26T16:29:27.677+00:00"),
                        jsonPath("$.createdAt").value("2025-02-25T17:30:00"),
                        jsonPath("$.updatedAt").value("2025-02-26T12:00:00")
                );
    }

    @Test
    void update_InvalidSession_ReturnsBadRequest() throws Exception {
        long sessionId = 1L;

        SessionDto invalidSessionDto = new SessionDto(); // Invalid session data

        String invalidSessionDtoJson = objectMapper.writeValueAsString(invalidSessionDto);

        mockMvc.perform(put("/api/session/{id}", sessionId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidSessionDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_InvalidId_ReturnsBadRequest() throws Exception {
        String invalidId = "invalid-id";

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated session name");
        sessionDto.setDate(Date.from(Instant.parse("2025-02-26T16:29:27.677Z")));
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Updated session description");
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        sessionDto.setCreatedAt(LocalDateTime.parse("2025-02-25T17:30:00"));
        sessionDto.setUpdatedAt(LocalDateTime.parse("2025-02-26T12:00:00"));

        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/{id}", invalidId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(sessionDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_SessionExists_ReturnsOk() throws Exception {
        long sessionId = 1L;

        mockMvc.perform(delete("/api/session/{id}", sessionId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/{id}", sessionId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_SessionNotFound_ReturnsNotFound() throws Exception {
        long sessionId = 999L; // Session with id 999 does not exist

        mockMvc.perform(delete("/api/session/{id}", sessionId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() throws Exception {
        String invalidId = "invalid-id";

        mockMvc.perform(delete("/api/session/{id}", invalidId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_ValidSessionAndUser_ReturnsOk() throws Exception {
        long sessionId = 1L;
        long userId = 2L;

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/{id}", sessionId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[1]").value(userId));
    }

    @Test
    void participate_SessionNotFound_ReturnsNotFound() throws Exception {
        long sessionId = 999L; // Session with id 999 does not exist
        long userId = 1L;

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void participate_UserNotFound_ReturnsNotFound() throws Exception {
        long sessionId = 1L;
        long userId = 999L; // User with id 999 does not exist

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void participate_InvalidSessionId_ReturnsBadRequest() throws Exception {
        String invalidSessionId = "invalid-id";
        long userId = 1L;

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", invalidSessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_InvalidUserId_ReturnsBadRequest() throws Exception {
        long sessionId = 1L;
        String invalidUserId = "invalid-id";

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, invalidUserId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_ValidSessionAndUser_ReturnsOk() throws Exception {
        long sessionId = 1L;
        long userId = 1L;

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/{id}", sessionId)
                        .headers(adminHttpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").isEmpty());
    }

    @Test
    void noLongerParticipate_SessionNotFound_ReturnsNotFound() throws Exception {
        long sessionId = 999L; // Session with id 999 does not exist
        long userId = 1L;

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void noLongerParticipate_UserNotFound_ReturnsNotFound() throws Exception {
        long sessionId = 1L;
        long userId = 999L; // User with id 999 does not exist

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_InvalidSessionId_ReturnsBadRequest() throws Exception {
        String invalidSessionId = "invalid-id";
        long userId = 1L;

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", invalidSessionId, userId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_InvalidUserId_ReturnsBadRequest() throws Exception {
        long sessionId = 1L;
        String invalidUserId = "invalid-id";

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, invalidUserId)
                            .headers(adminHttpHeaders)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
