package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.service.StudentService;
import org.example.service.TeacherService;
import org.example.service.impl.CourseServiceImpl;
import org.example.service.impl.StudentServiceImpl;
import org.example.service.impl.TeacherServiceImpl;
import org.example.servlet.dto.StudentIncomingDto;
import org.example.servlet.dto.StudentUpdateDto;
import org.example.servlet.dto.TeacherIncomingDto;
import org.example.servlet.dto.TeacherUpdateDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
class TeacherServletTest {
    private static TeacherService mockTeacherService;
    @InjectMocks
    private static TeacherServlet teacherServlet;
    private static TeacherServiceImpl oldInstance;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(TeacherService mock) {
        try {
            Field instance = TeacherServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (TeacherServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockTeacherService = Mockito.mock(TeacherService.class);
        setMock(mockTeacherService);
        teacherServlet = new TeacherServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = TeacherServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockTeacherService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("teacher/all").when(mockRequest).getPathInfo();

        teacherServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockTeacherService).findAll();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.doReturn("teacher/1").when(mockRequest).getPathInfo();

        teacherServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockTeacherService).findById(Mockito.anyLong());
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("teacher/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockTeacherService).findById(100L);

        teacherServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("student/2q").when(mockRequest).getPathInfo();

        teacherServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("student/2").when(mockRequest).getPathInfo();

        teacherServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockTeacherService).delete(Mockito.anyLong());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("student/a100").when(mockRequest).getPathInfo();

        teacherServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New teacher";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedName + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        teacherServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<TeacherIncomingDto> argumentCaptor = ArgumentCaptor.forClass(TeacherIncomingDto.class);
        Mockito.verify(mockTeacherService).save(argumentCaptor.capture());

        TeacherIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPutName() throws IOException, NotFoundException {
        String expectedFirstname = "New name";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\": 1," +
                        "\"name\":\"" + expectedFirstname + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        teacherServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<TeacherUpdateDto> argumentCaptor = ArgumentCaptor.forClass(TeacherUpdateDto.class);
        Mockito.verify(mockTeacherService).update(argumentCaptor.capture());

        TeacherUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedFirstname, result.getName());
    }

    @Test
    void doPutNameAndCourse() throws IOException, NotFoundException {
        String expectedName = "New name";
        String expectedCourseName = "New course";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 2," +
                            "\"name\": \"" + expectedName +
                            "\",\"course\": " +
                        "{\"id\": 1," +
                        "\"name\": \"" + expectedCourseName+
                        "\"}}",
                null
        ).when(mockBufferedReader).readLine();

        teacherServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<TeacherUpdateDto> argumentCaptor = ArgumentCaptor.forClass(TeacherUpdateDto.class);
        Mockito.verify(mockTeacherService).update(argumentCaptor.capture());

        TeacherUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
        Assertions.assertEquals(expectedCourseName, result.getCourse().getName());
    }

    @Test
    void doPutNameAndCourseDelete() throws IOException, NotFoundException {
        String expectedName = "New name";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 2," +
                        "\"name\": \"" + expectedName +
                        "\",\"course\": " +
                        "{\"id\": 1," +
                        "\"name\": \"delete\"}}",
                null
        ).when(mockBufferedReader).readLine();

        teacherServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<TeacherUpdateDto> argumentCaptor = ArgumentCaptor.forClass(TeacherUpdateDto.class);
        Mockito.verify(mockTeacherService).update(argumentCaptor.capture());

        TeacherUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{Bad json:1}",
                null
        ).when(mockBufferedReader).readLine();

        teacherServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
