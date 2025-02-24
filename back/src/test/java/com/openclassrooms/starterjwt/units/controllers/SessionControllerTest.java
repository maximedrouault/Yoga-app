package com.openclassrooms.starterjwt.units.controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;


    @Test
    void findById_ValidId_ReturnsSession() {
        Long sessionId = 1L;
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        when(sessionService.getById(sessionId)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById(sessionId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void findById_SessionDoesNotExist_ReturnsNotFound() {
        Long sessionId = 1L;
        when(sessionService.getById(sessionId)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById(sessionId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAll_SessionsExist_ReturnsListOfSessions() {
        List<Session> sessions = Collections.singletonList(new Session());
        List<SessionDto> sessionDtos = Collections.singletonList(new SessionDto());
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    void findAll_NoSessionsExist_ReturnsEmptyList() {
        when(sessionService.findAll()).thenReturn(Collections.emptyList());
        when(sessionMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void create_ValidSessionDto_ReturnsCreatedSession() {
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void update_ValidIdAndSessionDto_ReturnsUpdatedSession() {
        Long sessionId = 1L;
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(sessionId, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update(sessionId.toString(), sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void update_InvalidId_ReturnsBadRequest() {
        SessionDto sessionDto = new SessionDto();

        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void delete_ValidId_ReturnsOk() {
        Long sessionId = 1L;
        Session session = new Session();
        when(sessionService.getById(sessionId)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save(sessionId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void delete_SessionDoesNotExist_ReturnsNotFound() {
        Long sessionId = 1L;
        when(sessionService.getById(sessionId)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save(sessionId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void participate_ValidIds_ReturnsOk() {
        long sessionId = 1L;
        long userId = 1L;

        ResponseEntity<?> response = sessionController.participate(Long.toString(sessionId), Long.toString(userId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void participate_InvalidSessionId_ReturnsBadRequest() {
        long userId = 1L;

        ResponseEntity<?> response = sessionController.participate("invalid", Long.toString(userId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void noLongerParticipate_ValidIds_ReturnsOk() {
        long sessionId = 1L;
        long userId = 1L;

        ResponseEntity<?> response = sessionController.noLongerParticipate(Long.toString(sessionId), Long.toString(userId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void noLongerParticipate_InvalidSessionId_ReturnsBadRequest() {
        long userId = 1L;

        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", Long.toString(userId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
