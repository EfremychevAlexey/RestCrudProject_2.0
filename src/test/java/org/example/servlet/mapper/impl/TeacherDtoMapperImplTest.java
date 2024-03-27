package org.example.servlet.mapper.impl;

import org.example.model.Course;
import org.example.model.Teacher;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.TeacherDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TeacherDtoMapperImplTest {
    private TeacherDtoMapper teacherDtoMapper;

    @BeforeEach
    void setUp() {
        teacherDtoMapper = TeacherDtoMapperImpl.getInstance();
    }

    @DisplayName("Teacher map(TeacherIncomingDto)")
    @Test
    void mapIncoming() {
        TeacherIncomingDto dto = new TeacherIncomingDto("Teacher1");
        Teacher result = teacherDtoMapper.map(dto);
        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @DisplayName("TeacherOutGoingDto map(Teacher)")
    @Test
    void mapToOutgoing() {
        Teacher teacher = new Teacher(
                1L,
                "Teacher1",
                List.of(new Course(1L, "Course1", List.of(), List.of()))
        );

        TeacherOutGoingDto result = teacherDtoMapper.map(teacher);

        Assertions.assertEquals(teacher.getId(), result.getId());
        Assertions.assertEquals(teacher.getName(), result.getName());

        Assertions.assertEquals(teacher.getCourses().size(), result.getCourseList().size());
        Assertions.assertEquals(teacher.getCourses().get(0).getId(), result.getCourseList().get(0).getId());
        Assertions.assertEquals(teacher.getCourses().get(0).getName(), result.getCourseList().get(0).getName());
    }

    @Test
    void mapSmallOutGoing() {
        Teacher teacher = new Teacher(
                1L,
                "Teacher1",
                List.of(new Course(1L, "Course1", List.of(), List.of()))
        );

        TeacherSmallOutGoingDto result = teacherDtoMapper.mapSmallOutGoing(teacher);

        Assertions.assertEquals(teacher.getId(), result.getId());
        Assertions.assertEquals(teacher.getName(), result.getName());
    }

    @DisplayName("Teacher map (TeacherUpdateDto)")
    @Test
    void mapUpdateDto() {
        TeacherUpdateDto teacherUpdateDto = new TeacherUpdateDto(
                1L,
                "Teacher1",
                new CourseUpdateDto(1L, "Java")
        );

        Teacher result = teacherDtoMapper.map(teacherUpdateDto);

        Assertions.assertEquals(teacherUpdateDto.getId(), result.getId());
        Assertions.assertEquals(teacherUpdateDto.getName(), result.getName());
    }

    @DisplayName("List<TeacherOutGoingDto> map(List<Teacher>")
    @Test
    void mapToTeacherOutGoingList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(new Teacher(
                1L,
                "Teacher1",
                List.of(
                        new Course(1L, "Course1", List.of(), List.of()),
                        new Course(2L, "Course2", List.of(), List.of()))
        ));
        teacherList.add(new Teacher(
                2L,
                "Teacher2",
                List.of(
                        new Course(3L, "Course3", List.of(), List.of()),
                        new Course(4L, "Course4", List.of(), List.of()))
        ));

        List<TeacherOutGoingDto> result = teacherDtoMapper.map(teacherList);

        Assertions.assertEquals(teacherList.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(teacherList.get(0).getName(), result.get(0).getName());

        Assertions.assertEquals(teacherList.get(0).getCourses().size(), result.get(0).getCourseList().size());
        Assertions.assertEquals(teacherList.get(0).getCourses().get(0).getId(), result.get(0).getCourseList().get(0).getId());
        Assertions.assertEquals(teacherList.get(0).getCourses().get(0).getName(), result.get(0).getCourseList().get(0).getName());

        Assertions.assertEquals(teacherList.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(teacherList.get(1).getName(), result.get(1).getName());

        Assertions.assertEquals(teacherList.get(1).getCourses().size(), result.get(1).getCourseList().size());
        Assertions.assertEquals(teacherList.get(1).getCourses().get(1).getId(), result.get(1).getCourseList().get(1).getId());
        Assertions.assertEquals(teacherList.get(1).getCourses().get(1).getName(), result.get(1).getCourseList().get(1).getName());
    }

    @Test
    void mapSmallOutGoingList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(new Teacher(
                1L,
                "Teacher1",
                List.of(
                        new Course(1L, "Course1", List.of(), List.of()),
                        new Course(2L, "Course2", List.of(), List.of()))
        ));
        teacherList.add(new Teacher(
                2L,
                "Teacher2",
                List.of(
                        new Course(3L, "Course3", List.of(), List.of()),
                        new Course(4L, "Course4", List.of(), List.of()))
        ));

        List<TeacherSmallOutGoingDto> result = teacherDtoMapper.mapSmallOutGoingList(teacherList);

        Assertions.assertEquals(teacherList.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(teacherList.get(0).getName(), result.get(0).getName());

        Assertions.assertEquals(teacherList.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(teacherList.get(1).getName(), result.get(1).getName());
    }
}
