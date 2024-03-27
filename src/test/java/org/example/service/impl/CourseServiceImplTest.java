package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Course;
import org.example.model.CourseTeacher;
import org.example.model.Student;
import org.example.repositoryDAO.CourseDAO;
import org.example.repositoryDAO.CourseTeacherDAO;
import org.example.repositoryDAO.StudentDAO;
import org.example.repositoryDAO.TeacherDAO;
import org.example.repositoryDAO.impl.CourseDAOImpl;
import org.example.repositoryDAO.impl.CourseTeacherDAOImpl;
import org.example.repositoryDAO.impl.StudentDAOImpl;
import org.example.service.CourseService;
import org.example.servlet.dto.CourseIncomingDto;
import org.example.servlet.dto.CourseOutGoingDto;
import org.example.servlet.dto.CourseUpdateDto;
import org.example.servlet.mapper.CourseDtoMapper;
import org.example.servlet.mapper.impl.CourseDtoMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

class CourseServiceImplTest {

    private static CourseService courseService;
    private static CourseDAO mockCourseDao;
    private static StudentDAO mockStudentDao;
    private static CourseTeacherDAO mockCourseTeacherDao;

    private static CourseDAOImpl oldCourseInstance;
    private static StudentDAOImpl oldStudentInstance;
    private static CourseTeacherDAOImpl oldLinkInstance;

    private static void setMock(CourseDAO mock) {
        try {
            Field instance = CourseDAOImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldCourseInstance = (CourseDAOImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(StudentDAO mock) {
        try {
            Field instance = StudentDAOImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldStudentInstance = (StudentDAOImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(CourseTeacherDAO mock) {
        try {
            Field instance = CourseTeacherDAOImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldLinkInstance = (CourseTeacherDAOImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockCourseDao = Mockito.mock(CourseDAO.class);
        setMock(mockCourseDao);
        mockStudentDao = Mockito.mock(StudentDAO.class);
        setMock(mockStudentDao);
        mockCourseTeacherDao = Mockito.mock(CourseTeacherDAO.class);
        setMock(mockCourseTeacherDao);
        courseService = CourseServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = CourseDAOImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldCourseInstance);

        instance = StudentDAOImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldStudentInstance);

        instance = CourseTeacherDAOImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldLinkInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockCourseDao);
    }

    @Test
    void save() {
        Long expectedId = 1L;

        CourseIncomingDto dto = new CourseIncomingDto("f1 course");
        Course course = new Course(expectedId, "f1 course", List.of(), List.of());

        Mockito.doReturn(course).when(mockCourseDao).save(Mockito.any(Course.class));

        CourseOutGoingDto result = courseService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        CourseUpdateDto dto = new CourseUpdateDto(expectedId, "course update #1");

        Mockito.doReturn(true).when(mockCourseDao).existsById(Mockito.any());

        courseService.update(dto);

        ArgumentCaptor<Course> argumentCaptor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(mockCourseDao).update(argumentCaptor.capture());

        Course result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        CourseUpdateDto dto = new CourseUpdateDto(1L, "course update #1");

        Mockito.doReturn(false).when(mockCourseDao).existsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    courseService.update(dto);
                }, "Not found."
        );
        Assertions.assertEquals("Course not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Course> department = Optional.of(new Course(expectedId, "course found #1", List.of(), List.of()));

        Mockito.doReturn(true).when(mockCourseDao).existsById(Mockito.any());
        Mockito.doReturn(department).when(mockCourseDao).findById(Mockito.anyLong());

        CourseOutGoingDto dto = courseService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Optional<Course> course = Optional.empty();

        Mockito.doReturn(false).when(mockCourseDao).existsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    courseService.findById(1L);
                }, "Not found."
        );
        Assertions.assertEquals("Course not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        courseService.findAll();
        Mockito.verify(mockCourseDao).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 100L;

        Mockito.doReturn(true).when(mockCourseDao).existsById(Mockito.any());
        courseService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockCourseDao).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}
