package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Course;
import org.example.repositoryDAO.CourseDAO;
import org.example.repositoryDAO.impl.CourseDAOImpl;
import org.example.service.CourseService;
import org.example.servlet.dto.CourseIncomingDto;
import org.example.servlet.dto.CourseOutGoingDto;
import org.example.servlet.dto.CourseUpdateDto;
import org.example.servlet.mapper.CourseDtoMapper;
import org.example.servlet.mapper.impl.CourseDtoMapperImpl;

import java.util.List;
import java.util.Optional;

/**
 * Совершает операции в таблице courses
 */
public class CourseServiceImpl implements CourseService {
    private static CourseService instance;
    private static final CourseDAO courseDao = CourseDAOImpl.getInstance();
    private static final CourseDtoMapper courseDtoMapper = CourseDtoMapperImpl.getInstance();

    private CourseServiceImpl(){
    }

    /**
     * Возвращает экземпляр класса
     * @return
     */
    public static synchronized CourseService getInstance() {
        if (instance == null) {
            instance = new CourseServiceImpl();
        }
        return instance;
    }

    /**
     * Проверяем наличие записи в таблице по id
     * Если ее нет выбрасываем исключение
     * @param courseId
     * @throws NotFoundException
     */
    private void checkExistCourse(Long courseId) throws NotFoundException {
        if (!courseDao.existsById(courseId)) {
            throw new NotFoundException("Course not found.");
        }
    }

    /**
     * Преобразуем полученного CourseIncomingDto в модель Course
     * Сохраняем модель Course в бд, получаем модель Course с id
     * Преобразуем модель Course в CourseOutGoingDto и возвращаем её.
     * @param courseIncomingDto
     * @return
     */
    @Override
    public CourseOutGoingDto save(CourseIncomingDto courseIncomingDto) {
        Course course;
        Optional<Course> optionalCourse = courseDao.findByName(courseIncomingDto.getName());

        if (optionalCourse.isPresent()) {
            return courseDtoMapper.map(optionalCourse.get());
        } else {
            course = courseDao.save(courseDtoMapper.map(courseIncomingDto));
            return courseDtoMapper.map(courseDao.findById(course.getId()).orElse(course));
        }
    }

    /**
     * Проверяем полученный CourseUpdate на null, значение id на null.
     * Обновляем запись в бд.
     * Если такой записи нет, выбрасываем исключение с соответствующим исключением
     * @param courseDto
     * @throws NotFoundException
     */
    @Override
    public void update(CourseUpdateDto courseDto) throws NotFoundException {
        if (courseDto == null || courseDto.getId() == null) {
            throw  new IllegalArgumentException();
        }
        checkExistCourse(courseDto.getId());
        courseDao.update(courseDtoMapper.map(courseDto));
    }

    /**
     * В случае если в таблице courses нет записи с таким id, выбрасываем исключение
     * с соответствующим сообщением
     * В ином случае получаем запись, преобрауем в CourseOutGoingDto и возвращаем
     * @param courseId
     * @return
     * @throws NotFoundException
     */
    @Override
    public CourseOutGoingDto findById(Long courseId) throws NotFoundException {
        checkExistCourse(courseId);
        Course course = courseDao.findById(courseId).orElseThrow();
        return courseDtoMapper.map(course);
    }

    /**
     * Получаем все записи из таблицы courses
     * возвращаем их в виде CourseOutGoingDto
     * @return
     */
    @Override
    public List<CourseOutGoingDto> findAll() {
        List<Course> allCourses = courseDao.findAll();
        return courseDtoMapper.map(allCourses);
    }

    /**
     * Удаляем из таблицы courses запись по id
     * Если записи с таким id нет, выбрасываем исключение.
     * @param courseId
     * @throws NotFoundException
     */
    @Override
    public void delete(Long courseId) throws NotFoundException {
        checkExistCourse(courseId);
        courseDao.deleteById(courseId);
    }

}
