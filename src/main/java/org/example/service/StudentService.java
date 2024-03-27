package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.StudentIncomingDto;
import org.example.servlet.dto.StudentOutGoingDto;
import org.example.servlet.dto.StudentUpdateDto;

import java.util.List;

public interface StudentService {

    StudentOutGoingDto save(StudentIncomingDto studentDto);

    void update(StudentUpdateDto studentDto) throws NotFoundException;

    StudentOutGoingDto findById(Long studentId) throws NotFoundException;

    List<StudentOutGoingDto> findAll() throws NotFoundException;

    void delete(Long userId) throws NotFoundException;
}
