package org.example.servlet.dto;

import java.util.List;

/**
 * Класс DTO для выходящих данных
 */
public class CourseOutGoingDto {
    private Long id;
    private String name;
    private List<StudentSmallOutGoingDto> studentList;
    private List<TeacherSmallOutGoingDto> teacherList;

    public CourseOutGoingDto() {
    }

    public CourseOutGoingDto(
            Long id,
            String name,
            List<StudentSmallOutGoingDto> studentList,
            List<TeacherSmallOutGoingDto> teacherList) {
        this.id = id;
        this.name = name;
        this.studentList = studentList;
        this.teacherList = teacherList;
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

    public List<StudentSmallOutGoingDto> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentSmallOutGoingDto> studentList) {
        this.studentList = studentList;
    }

    public List<TeacherSmallOutGoingDto> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherSmallOutGoingDto> teacherList) {
        this.teacherList = teacherList;
    }
}

