package org.example.repositoryDAO;

import org.example.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDAO extends DAO<Course, Long>{

    Optional<Course> findByName(String name);
}
