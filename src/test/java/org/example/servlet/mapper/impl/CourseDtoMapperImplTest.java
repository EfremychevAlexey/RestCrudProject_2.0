package org.example.servlet.mapper.impl;

import org.example.model.Course;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.CourseDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CourseDtoMapperImplTest {
    private CourseDtoMapper courseDtoMapper;

    @BeforeEach
    void setUp() {
        courseDtoMapper = CourseDtoMapperImpl.getInstance();
    }

    @DisplayName("Course map(CourseIncomingDto)")
    @Test
    void mapIncoming() {
        CourseIncomingDto dto = new CourseIncomingDto(
                "course1"
        );

        Course result = courseDtoMapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }


    @DisplayName("Course map(CourseUpdateDto)")
    @Test
    void testMapUpdete() {
        CourseUpdateDto dto = new CourseUpdateDto(
                100L,
                "course2"
        );
        Course result = courseDtoMapper.map(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @Test
    void mapSmallCourseList() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course(1L, "Course1", List.of(), List.of()));
        courseList.add(new Course(2L, "Course2", List.of(), List.of()));

        List<CourseSmallOutGoingDto> result = courseDtoMapper.mapSmallCourseList(courseList);

        Assertions.assertEquals(courseList.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(courseList.get(0).getName(), result.get(0).getName());

        Assertions.assertEquals(courseList.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(courseList.get(1).getName(), result.get(1).getName());
    }

    @DisplayName("CourseOutGoingDto map(Course)")
    @Test
    void mapOutGoing() {
        Course course =  new Course(1L, "Name course",
                List.of(new Student(), new Student()),
                List.of(new Teacher(), new Teacher(), new Teacher()));

        CourseOutGoingDto result = courseDtoMapper.map(course);

        Assertions.assertEquals(course.getId(), result.getId());
        Assertions.assertEquals(course.getName(), result.getName());

        Assertions.assertEquals(course.getStudents().size(), result.getStudentList().size());
        Assertions.assertEquals(course.getTeachers().size(), result.getTeacherList().size());
    }

    @Test
    void mapSmallCourse() {
        Course course =  new Course(1L, "Name course",
                List.of(new Student(), new Student()),
                List.of(new Teacher(), new Teacher(), new Teacher()));

        CourseSmallOutGoingDto result = courseDtoMapper.mapSmallCourse(Optional.ofNullable(course));

        Assertions.assertEquals(course.getId(), result.getId());
        Assertions.assertEquals(course.getName(), result.getName());
    }

    @DisplayName("List<CourseOutGoingDto> map(List<Course>)")
    @Test
    void mapCourseToOutGoing() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course(1L, "Name course1",
                List.of(new Student(), new Student()),
                List.of(new Teacher(), new Teacher(), new Teacher())));
        courseList.add(new Course(2L, "Name course2",
                List.of(new Student()),
                List.of(new Teacher(),new Teacher())));

        List<CourseOutGoingDto> resultList = courseDtoMapper.map(courseList);

        Assertions.assertEquals(courseList.get(0).getId(), resultList.get(0).getId());
        Assertions.assertEquals(courseList.get(0).getName(), resultList.get(0).getName());

        Assertions.assertEquals(courseList.get(1).getId(), resultList.get(1).getId());
        Assertions.assertEquals(courseList.get(1).getName(), resultList.get(1).getName());

        Assertions.assertEquals(courseList.get(0).getStudents().size(), resultList.get(0).getStudentList().size());
        Assertions.assertEquals(courseList.get(0).getTeachers().size(), resultList.get(0).getTeacherList().size());

        Assertions.assertEquals(courseList.get(1).getStudents().size(), resultList.get(1).getStudentList().size());
        Assertions.assertEquals(courseList.get(1).getTeachers().size(), resultList.get(1).getTeacherList().size());
    }
}
