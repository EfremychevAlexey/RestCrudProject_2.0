package org.example.repositoryDAO.impl;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.model.CourseTeacher;
import org.example.repositoryDAO.CourseTeacherDAO;
import org.example.util.PropertiesUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
class CourseTeacherDAOImplTest {
    private static final String INIT_SQL = "sql/init.sql";
    public static CourseTeacherDAO courseTeacherDAO;
    private static int containerPort = 5432;
    private static int localPort = 5432;
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("mydb")
            .withUsername(PropertiesUtil.getProperties("db.username"))
            .withPassword(PropertiesUtil.getProperties("db.password"))
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ))
            .withInitScript(INIT_SQL);
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        courseTeacherDAO = CourseTeacherDAOImpl.getInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container,"");
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, INIT_SQL);
    }

    @Test
    void save() {
        Long expectedCourseId = 1L;
        Long expectedTeacherId = 1L;
        CourseTeacher link = new CourseTeacher(
                null,
                expectedCourseId,
                expectedTeacherId
        );
        link = courseTeacherDAO.save(link);
        System.out.println(link.getId());

        Optional<CourseTeacher> resultLink = courseTeacherDAO.findById(link.getId());

        Assertions.assertTrue(resultLink.isPresent());
        Assertions.assertEquals(expectedCourseId, resultLink.get().getCourseId());
        Assertions.assertEquals(expectedTeacherId, resultLink.get().getTeacherId());
    }

    @Test
    void update() {

        Long expectedCourseId = 1L;
        Long expectedTeacherId = 2L;

        CourseTeacher link =courseTeacherDAO.findById(2L).get();

        Long oldCourseId = link.getCourseId();
        Long oldTeacherId = link.getTeacherId();

        Assertions.assertNotEquals(expectedCourseId, oldCourseId);
        Assertions.assertNotEquals(expectedTeacherId, oldTeacherId);

        link.setCourseId(expectedCourseId);
        link.setTeacherId(expectedTeacherId);

        courseTeacherDAO.update(link);

        CourseTeacher resultLink = courseTeacherDAO.findById(2L).get();

        Assertions.assertEquals(link.getId(), resultLink.getId());
        Assertions.assertEquals(expectedCourseId, resultLink.getCourseId());
        Assertions.assertEquals(expectedTeacherId, resultLink.getTeacherId());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = courseTeacherDAO.findAll().size();

        CourseTeacher link = new CourseTeacher(null, 1L, 1L);
        link = courseTeacherDAO.save(link);

        int resultSizeBefore = courseTeacherDAO.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = courseTeacherDAO.deleteById(link.getId());
        int resultSizeAfter = courseTeacherDAO.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @Test
    void deleteByCourseId() {
        Boolean expectedValue = true;
        int expectedSize = 0;
        int expectedSizeBefore = courseTeacherDAO.findAllByCourseId(1L).size();
        Assertions.assertNotEquals(expectedSize, expectedSizeBefore);

        boolean resultDelete = courseTeacherDAO.deleteByCourseId(1L);
        int resultSizeAfter = courseTeacherDAO.findAllByCourseId(1L).size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @Test
    void deleteByTeacherId() {
        Boolean expectedValue = true;
        int expectedSize = 0;
        int expectedSizeBefore = courseTeacherDAO.findAllByTeacherId(1L).size();
        Assertions.assertNotEquals(expectedSize, expectedSizeBefore);

        boolean resultDelete = courseTeacherDAO.deleteByTeacherId(1L);
        int resultSizeAfter = courseTeacherDAO.findAllByTeacherId(1L).size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"

    })
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<CourseTeacher> courseTeacher = courseTeacherDAO.findById(expectedId);

        Assertions.assertEquals(expectedValue, courseTeacher.isPresent());

        if (courseTeacher.isPresent()) {
            Assertions.assertEquals(expectedId, courseTeacher.get().getId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 9;
        int resultSize = courseTeacherDAO.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "9, true",
            "1000, false"
    })
    void existById(Long courseTeacherId, Boolean expectedValue) {
        Boolean isCourseTeacherExist = courseTeacherDAO.existsById(courseTeacherId);

        Assertions.assertEquals(expectedValue, isCourseTeacherExist);
    }

    @Test
    void findAllByCourseId() {
        int expectedSize = 1;
        int resultSize = courseTeacherDAO.findAllByCourseId(1L).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @Test
    void findAllByTeacherId() {
        int expectedSize = 2;
        int resultSize = courseTeacherDAO.findAllByTeacherId(1L).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @Test
    void findCoursesByTeacherId() {
        int expectedSize = 5;
        int resultSize = courseTeacherDAO.findCoursesByTeacherId(3L).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @Test
    void findTeacherByCourseId() {
        int expectedSize = 2;
        int resultSize = courseTeacherDAO.findTeachersByCourseId(5L).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @Test
    void findByCourseIdAndTeacherId() {
        boolean expectedValue = true;
        Long expectedId = 9L;
        Long courseId = 5L;
        Long teacherId = 3L;
        Optional<CourseTeacher> courseTeacher = courseTeacherDAO.findByCourseIdAndTeacherId(courseId, teacherId);

        Assertions.assertEquals(expectedValue, courseTeacher.isPresent());

        if (courseTeacher.isPresent()) {
            Assertions.assertEquals(expectedId, courseTeacher.get().getId());
        }
    }
}
