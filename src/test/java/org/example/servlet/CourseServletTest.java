package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.NotFoundException;
import org.example.service.CourseService;
import org.example.service.impl.CourseServiceImpl;
import org.example.servlet.dto.CourseIncomingDto;
import org.example.servlet.dto.CourseUpdateDto;
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
public class CourseServletTest {
    private static CourseService mockCourseService;
    @InjectMocks
    private static CourseServlet courseServlet;
    private static CourseServiceImpl oldInstance;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(CourseService mock) {
        try {
            Field instance = CourseServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (CourseServiceImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockCourseService = Mockito.mock(CourseService.class);
        setMock(mockCourseService);
        courseServlet = new CourseServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = CourseServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockCourseService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("course/all").when(mockRequest).getPathInfo();

        courseServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockCourseService).findAll();
    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.doReturn("course/2").when(mockRequest).getPathInfo();

        courseServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockCourseService).findById(Mockito.anyLong());
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("course/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockCourseService).findById(100L);

        courseServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("course/2q").when(mockRequest).getPathInfo();

        courseServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("course/2").when(mockRequest).getPathInfo();

        courseServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockCourseService).delete(Mockito.anyLong());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("course/a100").when(mockRequest).getPathInfo();

        courseServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New course";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedName + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        courseServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<CourseIncomingDto> argumentCaptor = ArgumentCaptor.forClass(CourseIncomingDto.class);
        Mockito.verify(mockCourseService).save(argumentCaptor.capture());

        CourseIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedFirstname = "New name";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\": 1," +
                        "\"name\":\"" + expectedFirstname + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        courseServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<CourseUpdateDto> argumentCaptor = ArgumentCaptor.forClass(CourseUpdateDto.class);
        Mockito.verify(mockCourseService).update(argumentCaptor.capture());

        CourseUpdateDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedFirstname, result.getName());
    }

    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{Bad json:1}",
                null
        ).when(mockBufferedReader).readLine();

        courseServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }






















}
