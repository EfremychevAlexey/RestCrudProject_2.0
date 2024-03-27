package org.example.servlet.dto;


/**
 * Класс DTO для входящих данных
 */
public class TeacherIncomingDto {
    private String name;

    public TeacherIncomingDto() {
    }

    public TeacherIncomingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
