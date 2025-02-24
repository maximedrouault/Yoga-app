package com.openclassrooms.starterjwt.units.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void findAll_ReturnsListOfTeachers() {
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertEquals(teachers, result);
        verify(teacherRepository).findAll();
    }

    @Test
    void findById_ExistingId_ReturnsTeacher() {
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(teacherId);

        assertEquals(teacher, result);
        verify(teacherRepository).findById(teacherId);
    }

    @Test
    void findById_NonExistingId_ReturnsNull() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(teacherId);

        assertNull(result);
        verify(teacherRepository).findById(teacherId);
    }
}
