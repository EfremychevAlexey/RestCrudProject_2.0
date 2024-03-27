package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.TeacherIncomingDto;
import org.example.servlet.dto.TeacherOutGoingDto;
import org.example.servlet.dto.TeacherUpdateDto;

import java.util.List;

public interface TeacherService {

    TeacherOutGoingDto save(TeacherIncomingDto teacherDto);

    void update(TeacherUpdateDto teacherDto) throws NotFoundException;

    TeacherOutGoingDto findById(Long teacherId) throws NotFoundException;

    List<TeacherOutGoingDto> findAll();

    boolean delete(Long teacherId) throws NotFoundException;
}
