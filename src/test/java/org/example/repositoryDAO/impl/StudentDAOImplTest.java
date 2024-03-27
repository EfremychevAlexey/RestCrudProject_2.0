package org.example.repositoryDAO.impl;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.model.Course;
import org.example.model.Student;
import org.example.repositoryDAO.CourseDAO;
import org.example.repositoryDAO.StudentDAO;
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
class StudentDAOImplTest {
    private static final String INIT_SQL = "sql/init.sql";
    public static StudentDAO studentDAO;
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
        studentDAO = StudentDAOImpl.getInstance();
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
        String expectedName = "new Student";
        Student student = new Student(
                null,
                expectedName,
                null
        );
        student = studentDAO.save(student);
        Optional<Student> resultStudent = studentDAO.findById(student.getId());

        Assertions.assertTrue(resultStudent.isPresent());
        Assertions.assertEquals(expectedName, resultStudent.get().getName());
    }

    @Test
    void update() {
        String expectedName = "Update Student name";
        Student student = studentDAO.findById(2L).get();
        String oldName = student.getName();
        Long oldCourseID = student.getCourse().getId();
        String oldCourseName = student.getCourse().getName();
        student.setName(expectedName);

        Assertions.assertNotEquals(expectedName, oldName);
        studentDAO.update(student);

        String actualName = studentDAO.findById(2L).get().getName();
        Assertions.assertEquals(expectedName, actualName);

        Course courseUpdate = courseDAO.findById(4L).get();
        Long courseUpdateId = courseUpdate.getId();
        String courseUpdateName = courseUpdate.getName();

        Assertions.assertNotEquals(oldCourseID, courseUpdateId);
        Assertions.assertNotEquals(oldCourseName, courseUpdateName);

        student.setCourse(courseUpdate);
        studentDAO.update(student);

        Student studentUpdated = studentDAO.findById(2L).get();
        Long actualCourseId = studentUpdated.getCourse().getId();
        String actualCourseName = studentUpdated.getCourse().getName();

        Assertions.assertEquals(courseUpdateId, actualCourseId);
        Assertions.assertEquals(courseUpdateName, actualCourseName);

        Course courseNull = null;
        student.setCourse(courseNull);
        studentDAO.update(student);
        Student studentUpdatedCourseNull = studentDAO.findById(2L).get();

        Course actualCourse = studentUpdatedCourseNull.getCourse();
        Assertions.assertEquals(courseNull, actualCourse);
    }

    @Test
    void updateByCourseId() {
        Course expectedCourse = null;
        // Студент из базы
        Student student = studentDAO.findById(1L).get();
        Course course = student.getCourse();
        Long courseID = course.getId();

        Assertions.assertNotEquals(expectedCourse, course);

        studentDAO.deleteCourseIdByCourseId(courseID);

        Student studentUpdated = studentDAO.findById(1L).get();
        Course studentUpdatedGetCourse = studentUpdated.getCourse();
        Assertions.assertEquals(expectedCourse, studentUpdatedGetCourse);
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = studentDAO.findAll().size();

        Student tempStudent = new Student(null, "New student", null);
        tempStudent = studentDAO.save(tempStudent);

        int resultSizeBefore = studentDAO.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = studentDAO.deleteById(tempStudent.getId());
        int resultSizeAfter = studentDAO.findAll().size();

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
        Optional<Student> student = studentDAO.findById(expectedId);

        Assertions.assertEquals(expectedValue, student.isPresent());

        if (student.isPresent()) {
            Assertions.assertEquals(expectedId, student.get().getId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 4;
        int resultSize = studentDAO.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"
    })
    void existById(Long studentId, Boolean expectedValue) {
        boolean isStudentExist = studentDAO.existsById(studentId);
        Assertions.assertEquals(expectedValue, isStudentExist);
    }

    @Test
    void findAllByCourseId() {
        int expectedSize = 3;
        int resultSize = studentDAO.findAllByCourseId(1L).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }
}
