package org.example.servlet.dto;

import java.util.List;

/**
 * Класс DTO для выходящих данных
 */
public class TeacherOutGoingDto {
    private Long id;
    private String name;
    private List<CourseSmallOutGoingDto> courseList;

    public TeacherOutGoingDto() {
    }

    public TeacherOutGoingDto(Long id, String name, List<CourseSmallOutGoingDto> courseList) {
        this.id = id;
        this.name = name;
        this.courseList = courseList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseSmallOutGoingDto> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseSmallOutGoingDto> courseList) {
        this.courseList = courseList;
    }
}
