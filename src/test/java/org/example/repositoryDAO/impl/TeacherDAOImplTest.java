package org.example.repositoryDAO.impl;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.model.Teacher;
import org.example.repositoryDAO.CourseDAO;
import org.example.repositoryDAO.TeacherDAO;
import org.example.util.PropertiesUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
class TeacherDAOImplTest {
    private static final String INIT_SQL = "sql/init.sql";
    public static TeacherDAO teacherDAO;
    public static CourseDAO courseDAO;
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
        teacherDAO = TeacherDAOImpl.getInstance();
        courseDAO = CourseDAOImpl.getInstance();
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
        String expectedName = "new Teacher";
        Teacher teacher = new Teacher(
                null,
                expectedName,
                List.of()
        );
        teacher = teacherDAO.save(teacher);
        Optional<Teacher> resultTeacher = teacherDAO.findById(teacher.getId());

        Assertions.assertTrue(resultTeacher.isPresent());
        Assertions.assertEquals(expectedName, resultTeacher.get().getName());
    }

    @Test
    void update() {
        String expectedName = "Update Course name";

        Teacher teacher = teacherDAO.findById(2L).get();
        String oldName = teacher.getName();
        int expectedSizeCourseList = teacher.getCourses().size();

        teacher.setName(expectedName);
        teacherDAO.update(teacher);

        Teacher resultTeacher = teacherDAO.findById(2L).get();
        int resultSizeCourseList = resultTeacher.getCourses().size();

        Assertions.assertNotEquals(expectedName, oldName);
        Assertions.assertEquals(expectedName, resultTeacher.getName());
        Assertions.assertEquals(expectedSizeCourseList, resultSizeCourseList);
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = teacherDAO.findAll().size();

        Teacher tempTeacher = new Teacher(null, "New teacher", List.of());
        tempTeacher = teacherDAO.save(tempTeacher);

        int resultSizeBefore = teacherDAO.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = teacherDAO.deleteById(tempTeacher.getId());
        int resultSizeAfter = teacherDAO.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "1000, false"

    })
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Teacher> teacher = teacherDAO.findById(expectedId);

        Assertions.assertEquals(expectedValue, teacher.isPresent());

        if (teacher.isPresent()) {
            Assertions.assertEquals(expectedId, teacher.get().getId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 3;
        int resultSize = teacherDAO.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "1000, false"
    })
    void existById(Long courseId, Boolean expectedValue) {
        boolean isTeacherExist = teacherDAO.existsById(courseId);

        Assertions.assertEquals(expectedValue, isTeacherExist);
    }
}
