package com.openclassrooms.starterjwt.units.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperImplTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;


    @Test
    void toEntity_ValidSessionDto_ReturnsSession() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session 1");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Description 1");
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        sessionDto.setCreatedAt(LocalDateTime.now().minusDays(1));
        sessionDto.setUpdatedAt(LocalDateTime.now());

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherService.findById(1L)).thenReturn(teacher);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session session = sessionMapper.toEntity(sessionDto);

        assertAll(
                () -> assertEquals(sessionDto.getId(), session.getId()),
                () -> assertEquals(sessionDto.getName(), session.getName()),
                () -> assertEquals(sessionDto.getDate(), session.getDate()),
                () -> assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId()),
                () -> assertEquals(sessionDto.getDescription(), session.getDescription()),
                () -> assertEquals(sessionDto.getUsers().size(), session.getUsers().size()),
                () -> assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt()),
                () -> assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt())
        );
    }

    @Test
    void toEntity_ValidUserIdsWithNullUserInList_ReturnsSessionWithNullUserInList() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(null);

        Session session = sessionMapper.toEntity(sessionDto);

        assertAll(
                () -> assertEquals(2, session.getUsers().size()),
                () -> assertTrue(session.getUsers().contains(user1)),
                () -> assertNull(session.getUsers().get(1))
        );
    }

    @Test
    void toEntity_NullSessionDto_ReturnsNull() {
        Session session = sessionMapper.toEntity((SessionDto) null);

        assertNull(session);
    }

    @Test
    void toEntity_NullSessionDtoList_ReturnsNull() {
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);

        assertNull(sessions);
    }

    @Test
    void toEntity_EmptySessionDtoList_ReturnsEmptyList() {
        List<SessionDto> sessionDtoList = Collections.emptyList();

        List<Session> sessions = sessionMapper.toEntity(sessionDtoList);

        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
    }

    @Test
    void toEntity_ValidSessionDtoList_ReturnsSessionList() {
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<SessionDto> sessionDtoList = Arrays.asList(sessionDto1, sessionDto2);

        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");

        List<Session> sessions = sessionMapper.toEntity(sessionDtoList);

        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        assertEquals(session1, sessions.get(0));
        assertEquals(session2, sessions.get(1));
    }

    @Test
    void toDto_ValidSession_ReturnsSessionDto() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        session.setDescription("Description 1");
        session.setDate(new Date());
        session.setCreatedAt(LocalDateTime.now().minusDays(1));
        session.setUpdatedAt(LocalDateTime.now());

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertAll(
                () -> assertEquals(session.getId(), sessionDto.getId()),
                () -> assertEquals(session.getName(), sessionDto.getName()),
                () -> assertEquals(session.getDate(), sessionDto.getDate()),
                () -> assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id()),
                () -> assertEquals(session.getDescription(), sessionDto.getDescription()),
                () -> assertEquals(session.getUsers().size(), sessionDto.getUsers().size()),
                () -> assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt()),
                () -> assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt())
        );
    }

    @Test
    void toDto_NullSession_ReturnsNull() {
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        assertNull(sessionDto);
    }

    @Test
    void toDto_NullSessionList_ReturnsNull() {
        List<SessionDto> sessionDtos = sessionMapper.toDto((List<Session>) null);

        assertNull(sessionDtos);
    }

    @Test
    void toDto_EmptySessionList_ReturnsEmptyList() {
        List<Session> sessions = Collections.emptyList();

        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        assertNotNull(sessionDtos);
        assertTrue(sessionDtos.isEmpty());
    }

    @Test
    void toDto_ValidSessionList_ReturnsSessionDtoList() {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");

        List<Session> sessionList = Arrays.asList(session1, session2);

        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<SessionDto> sessionDtos = sessionMapper.toDto(sessionList);

        assertNotNull(sessionDtos);
        assertEquals(2, sessionDtos.size());
        assertAll(() -> {
            SessionDto sessionDto = sessionDtos.get(0);
            assertEquals(session1.getId(), sessionDto.getId());
            assertEquals(session1.getName(), sessionDto.getName());
            assertTrue(sessionDto.getUsers().isEmpty());
        }, () -> {
            SessionDto sessionDto = sessionDtos.get(1);
            assertEquals(session2.getId(), sessionDto.getId());
            assertEquals(session2.getName(), sessionDto.getName());
            assertTrue(sessionDto.getUsers().isEmpty());
        });
    }

    @Test
    void toDto_SessionWithTeacherWithoutId_ReturnsSessionDtoWithNullTeacherId() {
        Session session = new Session();
        Teacher teacher = new Teacher();
        session.setTeacher(teacher);

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNull(sessionDto.getTeacher_id());
    }
}
