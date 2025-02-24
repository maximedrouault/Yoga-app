package com.openclassrooms.starterjwt.units.controllers;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @Test
    void findById_ValidId_ReturnsUser() {
        Long userId = 1L;
        User user = new User();
        when(userService.findById(userId)).thenReturn(user);

        ResponseEntity<?> response = userController.findById(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userMapper).toDto(user);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = userController.findById(userId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_ValidId_DeletesUser() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.findById(userId)).thenReturn(user);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        ResponseEntity<?> response = userController.save(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).delete(userId);
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = userController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = userController.save(userId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_UnauthorizedUser_ReturnsUnauthorized() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.findById(userId)).thenReturn(user);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("other@example.com");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        ResponseEntity<?> response = userController.save(userId.toString());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
