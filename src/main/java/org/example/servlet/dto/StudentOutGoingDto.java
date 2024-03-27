package org.example.servlet.dto;

/**
 * Класс DTO для выходящих данных
 */
public class StudentOutGoingDto {
    private Long id;
    private String name;
    private CourseSmallOutGoingDto course;

    public StudentOutGoingDto() {
    }

    public StudentOutGoingDto(Long id, String name, CourseSmallOutGoingDto course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CourseSmallOutGoingDto getCourse() {
        return course;
    }
}
