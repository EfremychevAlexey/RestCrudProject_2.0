package org.example.repositoryDAO.impl;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.model.Course;
import org.example.repositoryDAO.CourseDAO;
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
class CourseDAOImplTest {
    private static final String INIT_SQL = "sql/init.sql";
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
        String expectedName = "new Course";
        Course course = new Course(
                null,
                expectedName,
                null,
                null
        );
        course = courseDAO.save(course);
        Optional<Course> resultCourse = courseDAO.findById(course.getId());

        Assertions.assertTrue(resultCourse.isPresent());
        Assertions.assertEquals(expectedName, resultCourse.get().getName());
    }

    @Test
    void update() {
        String expectedName = "Update Course name";

        Course course = courseDAO.findById(2L).get();
        String oldName = course.getName();
        int expectedSizeStudentList = course.getStudents().size();
        int expectedSizeTeacherList = course.getTeachers().size();
        course.setName(expectedName);
        courseDAO.update(course);

        Course resultCourse = courseDAO.findById(2L).get();
        int resultSizeStudentList = resultCourse.getStudents().size();
        int resultSizeTeacherList = resultCourse.getTeachers().size();

        Assertions.assertNotEquals(expectedName, oldName);
        Assertions.assertEquals(expectedName, resultCourse.getName());
        Assertions.assertEquals(expectedSizeStudentList, resultSizeStudentList);
        Assertions.assertEquals(expectedSizeTeacherList, resultSizeTeacherList);
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = 5;
        int expectedStudentInTheCourseSize = 3;
        int expectedTeachersInTheCourseSize = 1;

        Optional<Course> course = courseDAO.findById(1L);
        if (course.isPresent()) {
            int studentInTheCourseSize = course.get().getStudents().size();
            int teacherInTheCourseSize = course.get().getTeachers().size();
            Assertions.assertEquals(expectedStudentInTheCourseSize, studentInTheCourseSize);
            Assertions.assertEquals(expectedTeachersInTheCourseSize, teacherInTheCourseSize);

            int resultSizeBefore = courseDAO.findAll().size();
            Assertions.assertEquals(expectedSize, resultSizeBefore);

            boolean resultDelete = courseDAO.deleteById(course.get().getId());
            int resultSizeAfter = courseDAO.findAll().size();

            Assertions.assertEquals(expectedValue, resultDelete);
            Assertions.assertEquals(expectedSize - 1, resultSizeAfter);
        }
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"

    })
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Course> course = courseDAO.findById(expectedId);

        Assertions.assertEquals(expectedValue, course.isPresent());

        if (course.isPresent()) {
            Assertions.assertEquals(expectedId, course.get().getId());
        }
    }

    @DisplayName("Find by Name")
    @ParameterizedTest
    @CsvSource(value = {
            "Java; true",
            "PHP; true",
            "Photo; false"

    }, delimiter = ';')
    void findByName(String expectedName, Boolean expectedValue) {
        Optional<Course> course = courseDAO.findByName(expectedName);

        Assertions.assertEquals(expectedValue, course.isPresent());

        if (course.isPresent()) {
            Assertions.assertEquals(expectedName, course.get().getName());
        }
    }


    @Test
    void findAll() {
        int expectedSize = 5;
        int resultSize = courseDAO.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"
    })
    void existById(Long courseId, Boolean expectedValue) {
        boolean isCourseExist = courseDAO.existsById(courseId);

        Assertions.assertEquals(expectedValue, isCourseExist);
    }
}
