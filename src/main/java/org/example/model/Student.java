package org.example.model;

import java.util.Objects;

/**
 * Student entity
 * <p>
 * Relation:
 * Many To One: Student -> Course
 */

public class Student {
    private Long id;
    private String name;
    private Course course;

    public Student() {
    }

    public Student(Long id, String name, Course course) {
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

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}




