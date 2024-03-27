package org.example.servlet.mapper.impl;

import org.example.model.Teacher;
import org.example.servlet.dto.TeacherIncomingDto;
import org.example.servlet.dto.TeacherOutGoingDto;
import org.example.servlet.dto.TeacherSmallOutGoingDto;
import org.example.servlet.dto.TeacherUpdateDto;
import org.example.servlet.mapper.CourseDtoMapper;
import org.example.servlet.mapper.TeacherDtoMapper;

import java.util.List;

/**
 * Класс представляющий методы для преобразования DTO объектов
 */
public class TeacherDtoMapperImpl implements TeacherDtoMapper{

    private static final CourseDtoMapper courseDtoMapping = CourseDtoMapperImpl.getInstance();
    private static TeacherDtoMapper instance;

    private TeacherDtoMapperImpl() {
    }

    public static synchronized TeacherDtoMapper getInstance() {
        if (instance == null) {
            instance = new TeacherDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Teacher map(TeacherIncomingDto teacherIncomingDto) {
        return new Teacher(
                null,
                teacherIncomingDto.getName(),
                null);
    }

    @Override
    public TeacherOutGoingDto map(Teacher teacher) {
        return new TeacherOutGoingDto(
                teacher.getId(),
                teacher.getName(),
                courseDtoMapping.mapSmallCourseList(teacher.getCourses())
        );
    }

    @Override
    public TeacherSmallOutGoingDto mapSmallOutGoing(Teacher teacher) {
        return new TeacherSmallOutGoingDto(
                teacher.getId(),
                teacher.getName()
        );
    }

    @Override
    public Teacher map(TeacherUpdateDto teacherUpdateDto) {
        return new Teacher(
                teacherUpdateDto.getId(),
                teacherUpdateDto.getName(),
                null
        );
    }

    @Override
    public List<TeacherOutGoingDto> map(List<Teacher> teacherList) {
        return teacherList.stream().map(this::map).toList();
    }

    @Override
    public List<TeacherSmallOutGoingDto> mapSmallOutGoingList(List<Teacher> teacherList) {
        return teacherList.stream().map(this::mapSmallOutGoing).toList();
    }
}
