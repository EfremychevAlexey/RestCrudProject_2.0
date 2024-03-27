package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Student;
import org.example.repositoryDAO.StudentDAO;
import org.example.repositoryDAO.impl.StudentDAOImpl;
import org.example.service.StudentService;
import org.example.servlet.dto.CourseUpdateDto;
import org.example.servlet.dto.StudentIncomingDto;
import org.example.servlet.dto.StudentOutGoingDto;
import org.example.servlet.dto.StudentUpdateDto;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

public class StudentServiceImplTest {
    private static StudentService studentService;
    private  static StudentDAO mockStudentDao;
    private static StudentDAOImpl oldInstance;

    private static void setMock(StudentDAO mock) {
        try {
            Field instance = StudentDAOImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (StudentDAOImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockStudentDao = Mockito.mock(StudentDAO.class);
        setMock(mockStudentDao);
        studentService = StudentServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = StudentDAOImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockStudentDao);
    }

    @Test
    void save() {
        Long expectedId = 5L;

        StudentIncomingDto dto = new StudentIncomingDto("new student");
        Student student = new Student(expectedId, "new student", null);

        Mockito.doReturn(student).when(mockStudentDao).save(Mockito.any(Student.class));

        StudentOutGoingDto result = studentService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedStudentId = 1L;
        Long expectedCourseId = 1L;
        CourseUpdateDto courseUpdateDto = new CourseUpdateDto(expectedCourseId, "course1");

        StudentUpdateDto dto = new StudentUpdateDto(expectedStudentId, "new student", courseUpdateDto);

        Mockito.doReturn(true).when(mockStudentDao).existsById(Mockito.any());

        studentService.update(dto);

        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(mockStudentDao).update(argumentCaptor.capture());

        Student result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedStudentId, result.getId());
        Assertions.assertEquals(expectedCourseId, result.getCourse().getId());
    }

    @Test
    void updateNotFound() {
        StudentUpdateDto dto = new StudentUpdateDto(1L, "new name", null);

        Mockito.doReturn(false).when(mockStudentDao).existsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    studentService.update(dto);
                }, "Not found."
        );
        Assertions.assertEquals("Student not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Student> role = Optional.of(new Student(expectedId, "student", null));

        Mockito.doReturn(true).when(mockStudentDao).existsById(Mockito.any());
        Mockito.doReturn(role).when(mockStudentDao).findById(Mockito.anyLong());

        StudentOutGoingDto dto = studentService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Mockito.doReturn(false).when(mockStudentDao).existsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    studentService.findById(1L);
                }, "Not found."
        );
        Assertions.assertEquals("Student not found.", exception.getMessage());
    }

    @Test
    void findAll() throws NotFoundException {
        studentService.findAll();
        Mockito.verify(mockStudentDao).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 1L;

        Mockito.doReturn(true).when(mockStudentDao).existsById(Mockito.any());
        studentService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockStudentDao).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}
