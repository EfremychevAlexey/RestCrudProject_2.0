package org.example.servlet.dto;

/**
 * Класс для представления списка в использующих его других классах
 */
public class CourseSmallOutGoingDto {
    private Long id;
    private String name;

    public CourseSmallOutGoingDto() {
    }

    public CourseSmallOutGoingDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
