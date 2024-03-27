package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.*;

import java.util.List;

public interface CourseService {

    CourseOutGoingDto save(CourseIncomingDto courseDto);

    void update(CourseUpdateDto courseDto) throws NotFoundException;

    CourseOutGoingDto findById(Long courseId) throws NotFoundException;

    List<CourseOutGoingDto> findAll();

    void delete(Long courseId) throws NotFoundException;
}
