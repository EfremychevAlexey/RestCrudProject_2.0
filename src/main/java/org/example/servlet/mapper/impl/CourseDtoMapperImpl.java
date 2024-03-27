package org.example.servlet.mapper.impl;

import org.example.model.Course;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.CourseDtoMapper;
import org.example.servlet.mapper.StudentDtoMapper;
import org.example.servlet.mapper.TeacherDtoMapper;

import java.util.List;
import java.util.Optional;

/**
 * Класс представляющий методы для преобразования DTO объектов
 */
public class CourseDtoMapperImpl implements CourseDtoMapper {
    private static final TeacherDtoMapper teacherDtoMapper = TeacherDtoMapperImpl.getInstance();
    private static final StudentDtoMapper studentDtoMapper = StudentDtoMapperImpl.getInstance();

    private static CourseDtoMapper instance;

    public CourseDtoMapperImpl() {
    }

    public static synchronized CourseDtoMapper getInstance() {
        if (instance == null) {
            instance = new CourseDtoMapperImpl();
        }
        return instance;
    }


    @Override
    public Course map(CourseIncomingDto courseIncomingDto) {
        return new Course(
                null,
                courseIncomingDto.getName(),
                null,
                null
        );
    }

    @Override
    public CourseOutGoingDto map(Course course) {
        return new CourseOutGoingDto(
                course.getId(),
                course.getName(),
                studentDtoMapper.mapSmallOutGoingList(course.getStudents()),
                teacherDtoMapper.mapSmallOutGoingList(course.getTeachers())
        );
    }

    @Override
    public Course map(CourseUpdateDto courseUpdateDto) {
        if (courseUpdateDto == null) {
            return null;
        }
        return new Course(
                courseUpdateDto.getId(),
                courseUpdateDto.getName(),
                null,
                null
        );
    }

    @Override
    public List<CourseSmallOutGoingDto> mapSmallCourseList(List<Course> courseList) {
        return courseList.stream().map(course -> mapSmallCourse(Optional.ofNullable(course))).toList();
    }


    @Override
    public CourseSmallOutGoingDto mapSmallCourse(Optional<Course> optionalCourse) {
        CourseSmallOutGoingDto courseSmallOutGoingDto = null;
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            return new CourseSmallOutGoingDto(
                    course.getId(),
                    course.getName()
            );
        } else {
            return courseSmallOutGoingDto;
        }

    }

    @Override
    public List<CourseOutGoingDto> map(List<Course> courseList) {
        return courseList.stream().map(this::map).toList();
    }
}