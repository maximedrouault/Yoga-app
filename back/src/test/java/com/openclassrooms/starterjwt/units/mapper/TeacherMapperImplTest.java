package com.openclassrooms.starterjwt.units.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperImplTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);


    @Test
    void toEntity_ValidTeacherDto_ReturnsTeacher() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Smith");
        teacherDto.setFirstName("John");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now().minusDays(1));

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertAll(
                () -> assertEquals(teacherDto.getId(), teacher.getId()),
                () -> assertEquals(teacherDto.getLastName(), teacher.getLastName()),
                () -> assertEquals(teacherDto.getFirstName(), teacher.getFirstName()),
                () -> assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt()),
                () -> assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt())
        );
    }

    @Test
    void toEntity_NullTeacherDto_ReturnsNull() {
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        assertNull(teacher);
    }

    @Test
    void toDto_ValidTeacher_ReturnsTeacherDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("John")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertAll(
                () -> assertEquals(teacher.getId(), teacherDto.getId()),
                () -> assertEquals(teacher.getLastName(), teacherDto.getLastName()),
                () -> assertEquals(teacher.getFirstName(), teacherDto.getFirstName()),
                () -> assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt()),
                () -> assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt())
        );
    }

    @Test
    void toDto_NullTeacher_ReturnsNull() {
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);

        assertNull(teacherDto);
    }

    @Test
    void toEntity_ValidTeacherDtoList_ReturnsTeacherList() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Smith");
        teacherDto.setFirstName("John");
        teacherDto.setCreatedAt(LocalDateTime.now().minusDays(1));
        teacherDto.setUpdatedAt(LocalDateTime.now());

        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(teacherDto);

        List<Teacher> teachers = teacherMapper.toEntity(teacherDtos);

        assertEquals(teacherDtos.size(), teachers.size());
        assertAll(() -> {
            Teacher teacher = teachers.get(0);
            assertEquals(teacherDto.getId(), teacher.getId());
            assertEquals(teacherDto.getLastName(), teacher.getLastName());
            assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
            assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
            assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
        });
    }

    @Test
    void toEntity_NullTeacherDtoList_ReturnsNull() {
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);

        assertNull(teachers);
    }

    @Test
    void toDto_ValidTeacherList_ReturnsTeacherDtoList() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("John")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        assertEquals(teachers.size(), teacherDtos.size());
        assertAll(() -> {
            TeacherDto teacherDto = teacherDtos.get(0);
            assertEquals(teacher.getId(), teacherDto.getId());
            assertEquals(teacher.getLastName(), teacherDto.getLastName());
            assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
            assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
            assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
        });
    }

    @Test
    void toDto_NullTeacherList_ReturnsNull() {
        List<TeacherDto> teacherDtos = teacherMapper.toDto((List<Teacher>) null);

        assertNull(teacherDtos);
    }
}
