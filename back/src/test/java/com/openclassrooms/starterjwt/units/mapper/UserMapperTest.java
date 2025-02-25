package com.openclassrooms.starterjwt.units.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    @Test
    void ToDto_ValidUser_ReturnsUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
        user.setUpdatedAt(LocalDateTime.now());

        UserDto userDto = userMapper.toDto(user);

        assertAll(
                () -> assertEquals(user.getId(), userDto.getId()),
                () -> assertEquals(user.getEmail(), userDto.getEmail()),
                () -> assertEquals(user.getLastName(), userDto.getLastName()),
                () -> assertEquals(user.getFirstName(), userDto.getFirstName()),
                () -> assertEquals(user.isAdmin(), userDto.isAdmin()),
                () -> assertEquals(user.getPassword(), userDto.getPassword()),
                () -> assertEquals(user.getCreatedAt(), userDto.getCreatedAt()),
                () -> assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt())
        );
    }

    @Test
    void toDto_NullUser_ReturnsNull() {
        UserDto userDto = userMapper.toDto((User) null);

        assertNull(userDto);
    }

    @Test
    void toDto_ValidUserList_ReturnsUserDtoList() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
        user.setUpdatedAt(LocalDateTime.now());

        List<User> users = new ArrayList<>();
        users.add(user);

        List<UserDto> userDtos = userMapper.toDto(users);

        assertEquals(users.size(), userDtos.size());
        assertAll(() -> {
            UserDto userDto = userDtos.get(0);
            assertEquals(user.getId(), userDto.getId());
            assertEquals(user.getEmail(), userDto.getEmail());
            assertEquals(user.getLastName(), userDto.getLastName());
            assertEquals(user.getFirstName(), userDto.getFirstName());
            assertEquals(user.isAdmin(), userDto.isAdmin());
            assertEquals(user.getPassword(), userDto.getPassword());
            assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
            assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
        });

    }

    @Test
    void toDto_NullUserList_ReturnsNull() {
        List<UserDto> userDtos = userMapper.toDto((List<User>) null);

        assertNull(userDtos);
    }

    @Test
    void toDto_EmptyUserList_ReturnsEmptyUserDtoList() {
        List<User> users = new ArrayList<>();
        List<UserDto> userDtos = userMapper.toDto(users);

        assertTrue(userDtos.isEmpty());
    }

    @Test
    void ToEntity_ValidUserDTO_ReturnsUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("john.doe@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now().minusDays(1));
        userDto.setUpdatedAt(LocalDateTime.now());

        User user = userMapper.toEntity(userDto);

        assertAll(
                () -> assertEquals(userDto.getId(), user.getId()),
                () -> assertEquals(userDto.getEmail(), user.getEmail()),
                () -> assertEquals(userDto.getLastName(), user.getLastName()),
                () -> assertEquals(userDto.getFirstName(), user.getFirstName()),
                () -> assertEquals(userDto.isAdmin(), user.isAdmin()),
                () -> assertEquals(userDto.getPassword(), user.getPassword()),
                () -> assertEquals(userDto.getCreatedAt(), user.getCreatedAt()),
                () -> assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt())
        );
    }

    @Test
    void toEntity_NullUserDto_ReturnsNull() {
        User user = userMapper.toEntity((UserDto) null);

        assertNull(user);
    }

    @Test
    void toEntity_ValidUserDtoList_ReturnsUserList() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("john.doe@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now().minusDays(1));
        userDto.setUpdatedAt(LocalDateTime.now());

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);

        List<User> users = userMapper.toEntity(userDtos);

        assertEquals(userDtos.size(), users.size());
        assertAll(() -> {
            User user = users.get(0);
            assertEquals(userDto.getId(), user.getId());
            assertEquals(userDto.getEmail(), user.getEmail());
            assertEquals(userDto.getLastName(), user.getLastName());
            assertEquals(userDto.getFirstName(), user.getFirstName());
            assertEquals(userDto.isAdmin(), user.isAdmin());
            assertEquals(userDto.getPassword(), user.getPassword());
            assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
            assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
        });
    }

    @Test
    void toEntity_NullUserDtoList_ReturnsNull() {
        List<User> users = userMapper.toEntity((List<UserDto>) null);

        assertNull(users);
    }

    @Test
    void toEntity_EmptyUserDtoList_ReturnsEmptyUserList() {
        List<UserDto> userDtos = new ArrayList<>();
        List<User> users = userMapper.toEntity(userDtos);

        assertTrue(users.isEmpty());
    }
}
