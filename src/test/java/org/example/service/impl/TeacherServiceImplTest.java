package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Teacher;
import org.example.repositoryDAO.TeacherDAO;
import org.example.repositoryDAO.impl.TeacherDAOImpl;
import org.example.service.TeacherService;
import org.example.servlet.dto.CourseUpdateDto;
import org.example.servlet.dto.TeacherIncomingDto;
import org.example.servlet.dto.TeacherOutGoingDto;
import org.example.servlet.dto.TeacherUpdateDto;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class TeacherServiceImplTest {
    private static TeacherService teacherService;
    private static TeacherDAO mockTeacherDao;
    private static TeacherDAOImpl oldInstance;

    private static void setMock(TeacherDAO mock) {
        try {
            Field instance = TeacherDAOImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (TeacherDAOImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockTeacherDao = Mockito.mock(TeacherDAO.class);
        setMock(mockTeacherDao);
        teacherService = TeacherServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = TeacherDAOImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockTeacherDao);
    }

    @Test
    void save() {
        Long expectedId = 1L;

        TeacherIncomingDto dto = new TeacherIncomingDto("name");
        Teacher teacher = new Teacher(expectedId, "name", List.of());

        Mockito.doReturn(teacher).when(mockTeacherDao).save(Mockito.any(Teacher.class));

        TeacherOutGoingDto result = teacherService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        CourseUpdateDto courseUpdateDto = new CourseUpdateDto(1L, "nameCourse");
        TeacherUpdateDto dto = new TeacherUpdateDto(expectedId, "name", courseUpdateDto);

        Mockito.doReturn(true).when(mockTeacherDao).existsById(Mockito.any());

        teacherService.update(dto);

        ArgumentCaptor<Teacher> argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        Mockito.verify(mockTeacherDao).update(argumentCaptor.capture());

        Teacher result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        CourseUpdateDto courseUpdateDto = new CourseUpdateDto(1L, "nameCourse");
        TeacherUpdateDto dto = new TeacherUpdateDto(1L, "name", courseUpdateDto);

        Mockito.doReturn(false).when(mockTeacherDao).existsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    teacherService.update(dto);
                }, "Not found."
        );
        Assertions.assertEquals("Teacher not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Teacher> teacher = Optional.of(new Teacher(expectedId, "name", List.of()));

        Mockito.doReturn(true).when(mockTeacherDao).existsById(Mockito.any());
        Mockito.doReturn(teacher).when(mockTeacherDao).findById(Mockito.anyLong());

        TeacherOutGoingDto dto = teacherService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Optional<Teacher> user = Optional.empty();

        Mockito.doReturn(false).when(mockTeacherDao).existsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    teacherService.findById(1L);
                }, "Not found."
        );
        Assertions.assertEquals("Teacher not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        teacherService.findAll();
        Mockito.verify(mockTeacherDao).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 100L;

        Mockito.doReturn(true).when(mockTeacherDao).existsById(Mockito.any());
        teacherService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTeacherDao).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}
