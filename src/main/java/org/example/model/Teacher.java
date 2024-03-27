package org.example.model;

import org.example.repositoryDAO.CourseTeacherDAO;
import org.example.repositoryDAO.impl.CourseTeacherDAOImpl;

import java.util.List;

/**
 * Teacher entity
 * <p>
 * Relation:
 * Many To Many: Teacher <-> Course
 */

public class Teacher {
    private static final CourseTeacherDAO coursesTeachersDAO = CourseTeacherDAOImpl.getInstance();
    private Long id;
    private String name;
    private List<Course> courseList;

    public Teacher() {
    }

    public Teacher(Long id, String name, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.courseList = courses;
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

    public List<Course> getCourses() {
        if (courseList == null) {
            this.courseList = coursesTeachersDAO.findCoursesByTeacherId(this.id);
        }
        return courseList;
    }

    public void setCourses(List<Course> courseList) {
        this.courseList = courseList;
    }
}
