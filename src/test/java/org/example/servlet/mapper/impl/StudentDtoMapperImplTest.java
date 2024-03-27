package org.example.servlet.mapper.impl;

import org.example.model.Course;
import org.example.model.Student;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.StudentDtoMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StudentDtoMapperImplTest {
    private StudentDtoMapper studentDtoMapper;

    @BeforeEach
    void setUp() {
        studentDtoMapper = StudentDtoMapperImpl.getInstance();
    }

    @DisplayName("Student map(StudentIncomingDto)")
    @Test
    void mapIncoming() {
        StudentIncomingDto dto = new StudentIncomingDto("Student1");

        Student result = studentDtoMapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @DisplayName("StudentOutGoingDto map(Student student)")
    @Test
    void mapStudentToOutGoing() {
        Student dto = new Student(
                1L,
                "StudentUpdate",
                new Course(1L, "CourseUpdate", List.of(), List.of()));

        StudentOutGoingDto result = studentDtoMapper.map(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());

        Assertions.assertEquals(dto.getCourse().getName(), result.getCourse().getName());
    }

    @Test
    void mapSmallOutGoing() {
        Student student = new Student(
                1L,
                "StudentUpdate",
                new Course(1L, "CourseUpdate", List.of(), List.of()));

        StudentSmallOutGoingDto result = studentDtoMapper.mapSmallOutGoing(student);

        Assertions.assertEquals(student.getId(), result.getId());
        Assertions.assertEquals(student.getName(), result.getName());
    }

    @DisplayName("List<StudentOutGoingDto> map(List<Student>")
    @Test
    void mapToOutGoingList() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(
                1L,
                "Student1",
                new Course(2L, "Course1", List.of(), List.of())));
        studentList.add(new Student(
                2L,
                "Student2",
                new Course(2L, "Course2", List.of(), List.of())));

        List<StudentOutGoingDto> result = studentDtoMapper.map(studentList);

        Assertions.assertEquals(studentList.size(),result.size());

        Assertions.assertEquals(studentList.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(studentList.get(0).getName(), result.get(0).getName());

        Assertions.assertEquals(studentList.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(studentList.get(1).getName(), result.get(1).getName());

        Assertions.assertEquals(studentList.get(0).getCourse().getId(), result.get(0).getCourse().getId());
        Assertions.assertEquals(studentList.get(0).getCourse().getName(), result.get(0).getCourse().getName());

        Assertions.assertEquals(studentList.get(1).getCourse().getId(), result.get(1).getCourse().getId());
        Assertions.assertEquals(studentList.get(1).getCourse().getName(), result.get(1).getCourse().getName());

    }

    @Test
    void mapSmallOutGoingList() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(
                1L,
                "Student1",
                new Course(2L, "Course1", List.of(), List.of())));
        studentList.add(new Student(
                2L,
                "Student2",
                new Course(2L, "Course2", List.of(), List.of())));

        List<StudentSmallOutGoingDto> result = studentDtoMapper.mapSmallOutGoingList(studentList);

        Assertions.assertEquals(studentList.size(),result.size());

        Assertions.assertEquals(studentList.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(studentList.get(0).getName(), result.get(0).getName());

        Assertions.assertEquals(studentList.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(studentList.get(1).getName(), result.get(1).getName());
    }

    @DisplayName("Student map(StudentUpdateDto studentUpdateDto)")
    @Test
    void mapStudentUpdateDto() {
        StudentUpdateDto dto = new StudentUpdateDto(
                1L,
                "StudentUpdate",
                new CourseUpdateDto(1L, "CourseUpdate"));

        Student result = studentDtoMapper.map(dto);

        Assertions.assertEquals(dto.getId(),result.getId());
        Assertions.assertEquals(dto.getName(),result.getName());

        Assertions.assertEquals(dto.getCourse().getId(),result.getCourse().getId());
        Assertions.assertEquals(dto.getCourse().getName(),result.getCourse().getName());

    }
}
