package org.example.servlet.dto;

/**
 * Класс DTO для входящих данных
 */
public class StudentIncomingDto {
    private String name;

    public StudentIncomingDto() {
    }

    public StudentIncomingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
