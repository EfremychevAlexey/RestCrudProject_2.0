package org.example.model;

import org.example.repositoryDAO.CourseTeacherDAO;
import org.example.repositoryDAO.StudentDAO;
import org.example.repositoryDAO.impl.CourseTeacherDAOImpl;
import org.example.repositoryDAO.impl.StudentDAOImpl;

import java.util.List;

/**
 * Course entity
 * Курс на котором преподают учителя и учится студенты
 * Relation:
 * One To Many: Course <- Student
 * Many To Many: Course <-> Teacher
 */

public class Course {
    private static final CourseTeacherDAO coursesTeachersDAO = CourseTeacherDAOImpl.getInstance();
    private static final StudentDAO studentDAO = StudentDAOImpl.getInstance();
    private Long id;
    private String name;
    private List<Student> studentList;
    private List<Teacher> teacherList;

    public Course() {
    }

    public Course(Long id, String name, List<Student> studentList, List<Teacher> teacherList) {
        this.id = id;
        this.name = name;
        this.studentList = studentList;
        this.teacherList = teacherList;
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

    public List<Student> getStudents() {
        if (studentList == null) {
            studentList = studentDAO.findAllByCourseId(this.id);
        }
        return studentList;
    }

    public void setStudents(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Teacher> getTeachers() {
        if (teacherList == null) {
            teacherList = coursesTeachersDAO.findTeachersByCourseId(this.id);
        }
        return teacherList;
    }

    public void setTeachers(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }
}
