package org.example.servlet.dto;

/**
 * Класс представляющий данные для обновления записи в таблице бд
 */
public class CourseUpdateDto {
    private Long id;
    private String name;

    public CourseUpdateDto() {
    }

    public CourseUpdateDto(Long id, String name) {
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
