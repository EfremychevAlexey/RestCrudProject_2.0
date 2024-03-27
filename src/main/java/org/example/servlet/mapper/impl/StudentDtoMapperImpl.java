package org.example.servlet.mapper.impl;

import org.example.model.Student;
import org.example.servlet.dto.StudentIncomingDto;
import org.example.servlet.dto.StudentOutGoingDto;
import org.example.servlet.dto.StudentSmallOutGoingDto;
import org.example.servlet.dto.StudentUpdateDto;
import org.example.servlet.mapper.CourseDtoMapper;
import org.example.servlet.mapper.StudentDtoMapper;

import java.util.List;
import java.util.Optional;

/**
 * Класс представляющий методы для преобразования DTO объектов
 */
public class StudentDtoMapperImpl implements StudentDtoMapper {
    private static StudentDtoMapper instance;
    private static final CourseDtoMapper courseDtoMapper = CourseDtoMapperImpl.getInstance();

    private StudentDtoMapperImpl() {
    }

    public static synchronized StudentDtoMapper getInstance() {
        if (instance == null) {
            instance = new StudentDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Student map(StudentIncomingDto studentIncomingDto) {
        return new Student(
                null,
                studentIncomingDto.getName(),
                null
        );
    }

    @Override
    public StudentOutGoingDto map(Student student) {
        return new StudentOutGoingDto(
                student.getId(),
                student.getName(),
                courseDtoMapper.mapSmallCourse(Optional.ofNullable(student.getCourse()))
        );
    }

    @Override
    public StudentSmallOutGoingDto mapSmallOutGoing(Student student) {
        return new StudentSmallOutGoingDto(
                student.getId(),
                student.getName()
        );
    }

    @Override
    public List<StudentOutGoingDto> map(List<Student> studentList) {
        return studentList.stream().map(this::map).toList();
    }

    @Override
    public List<StudentSmallOutGoingDto> mapSmallOutGoingList(List<Student> studentList) {
        return studentList.stream().map(this::mapSmallOutGoing).toList();
    }

    @Override
    public Student map(StudentUpdateDto studentUpdateDto) {
        return new Student(
                studentUpdateDto.getId(),
                studentUpdateDto.getName(),
                courseDtoMapper.map(studentUpdateDto.getCourse())
        );
    }
}
