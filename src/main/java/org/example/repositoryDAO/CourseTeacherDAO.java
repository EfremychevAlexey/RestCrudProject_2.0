package org.example.repositoryDAO;

import org.example.model.Course;
import org.example.model.CourseTeacher;
import org.example.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface CourseTeacherDAO extends DAO<CourseTeacher, Long>{
    boolean deleteByCourseId(Long courseId);

    boolean deleteByTeacherId(Long teacherId);

    List<CourseTeacher> findAllByCourseId(Long courseId);

    List<CourseTeacher> findAllByTeacherId(Long teacherId);

    List<Course> findCoursesByTeacherId(Long teacherId);

    List<Teacher> findTeachersByCourseId(Long courseId);

    Optional<CourseTeacher> findByCourseIdAndTeacherId(Long courseId, Long teacherId);
}
