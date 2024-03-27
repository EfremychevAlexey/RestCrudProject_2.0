package org.example.servlet.dto;

/**
 * Класс DTO для входящих данных
 */
public class CourseIncomingDto {
    private String name;

    public CourseIncomingDto() {
    }

    public CourseIncomingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
