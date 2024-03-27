package org.example.servlet.mapper;

import org.example.model.Student;
import org.example.servlet.dto.StudentIncomingDto;
import org.example.servlet.dto.StudentOutGoingDto;
import org.example.servlet.dto.StudentSmallOutGoingDto;
import org.example.servlet.dto.StudentUpdateDto;

import java.util.List;

public interface StudentDtoMapper {

    Student map(StudentIncomingDto studentIncomingDto);

    StudentOutGoingDto map(Student student);

    StudentSmallOutGoingDto mapSmallOutGoing(Student student);

    List<StudentOutGoingDto> map(List<Student> studentList);

    List<StudentSmallOutGoingDto> mapSmallOutGoingList(List<Student> studentList);

    Student map(StudentUpdateDto studentUpdateDto);
}
