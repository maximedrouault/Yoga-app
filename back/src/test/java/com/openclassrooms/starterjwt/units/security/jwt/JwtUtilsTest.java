package com.openclassrooms.starterjwt.units.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;


    @Test
    void validateJwtToken_InvalidToken_ReturnsFalse() {
        String invalidToken = "invalidToken";

        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
}