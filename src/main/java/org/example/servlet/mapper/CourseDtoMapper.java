package org.example.servlet.mapper;

import org.example.model.Course;
import org.example.servlet.dto.CourseIncomingDto;
import org.example.servlet.dto.CourseOutGoingDto;
import org.example.servlet.dto.CourseSmallOutGoingDto;
import org.example.servlet.dto.CourseUpdateDto;

import java.util.List;
import java.util.Optional;

public interface CourseDtoMapper {
    Course map(CourseIncomingDto courseIncomingDto);

    Course map(CourseUpdateDto courseUpdateDto);

    List<CourseSmallOutGoingDto> mapSmallCourseList(List<Course> courseList);

    CourseOutGoingDto map(Course course);

    CourseSmallOutGoingDto mapSmallCourse(Optional<Course> optionalCourse);

    List<CourseOutGoingDto> map(List<Course> courseList);
}
