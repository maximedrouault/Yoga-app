package com.openclassrooms.starterjwt.units.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }


    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        String token = "validToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("testUser");

        UserDetails userDetails = User
                .withUsername("testUser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        authTokenFilter.doFilter(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        verify(jwtUtils).validateJwtToken(token);
        verify(userDetailsService).loadUserByUsername("testUser");
    }

    @Test
    void doFilterInternal_InvalidToken_DoesNotSetAuthentication() throws ServletException, IOException {
        String token = "invalidToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils).validateJwtToken(token);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternal_ExceptionThrown_LogsError() throws ServletException, IOException {
        String token = "errorToken";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        when(jwtUtils.validateJwtToken(token)).thenThrow(new RuntimeException("Unexpected error"));

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils).validateJwtToken(token);
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader_DoesNotSetAuthentication() throws ServletException, IOException {
        // No Authorization header

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtUtils, userDetailsService);
    }

    @Test
    void doFilterInternal_InvalidAuthorizationHeader_DoesNotSetAuthentication() throws ServletException, IOException {
        // Malformed Authorization header
        request.addHeader(HttpHeaders.AUTHORIZATION, "InvalidTokenFormat");

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtUtils, userDetailsService);
    }
}
