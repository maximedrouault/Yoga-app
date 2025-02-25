package com.openclassrooms.starterjwt.units.security.services;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @InjectMocks
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .admin(true)
                .password("password")
                .build();
    }


    @Test
    void getAuthorities_ReturnsEmptyCollection() {
        boolean result = userDetails.getAuthorities().isEmpty();

        assertTrue(result);
    }

    @Test
    void isAccountNonExpired_ReturnsTrue() {
        boolean result = userDetails.isAccountNonExpired();

        assertTrue(result);
    }

    @Test
    void isAccountNonLocked_ReturnsTrue() {
        boolean result = userDetails.isAccountNonLocked();

        assertTrue(result);
    }

    @Test
    void isCredentialsNonExpired_ReturnsTrue() {
        boolean result = userDetails.isCredentialsNonExpired();

        assertTrue(result);
    }

    @Test
    void isEnabled_ReturnsTrue() {
        boolean result = userDetails.isEnabled();

        assertTrue(result);
    }

    @Test
    void equals_SameObject_ReturnsTrue() {
        boolean result = userDetails.equals(userDetails);

        assertTrue(result);
    }

    @Test
    void equals_NotSameObject_ReturnsFalse() {
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(2L)
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .admin(true)
                .password("password")
                .build();

        boolean result = userDetails.equals(userDetails2);

        assertFalse(result);
    }

    @Test
    void equals_NullObject_ReturnsFalse() {
        boolean result = userDetails.equals(null);

        assertFalse(result);
    }

    @Test
    void equals_NotSameClassObject_ReturnsFalse() {
        Object userDetails2 = new Object();

        boolean result = userDetails.equals(userDetails2);

        assertFalse(result);
    }
}
