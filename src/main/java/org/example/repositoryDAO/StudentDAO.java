package org.example.repositoryDAO;

import org.example.model.Student;

import java.util.List;

public interface StudentDAO extends  DAO<Student, Long>{

    void deleteCourseIdByCourseId(Long courseId);
    List<Student> findAllByCourseId(Long courseId);
}
