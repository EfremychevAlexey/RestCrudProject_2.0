package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.CourseTeacher;
import org.example.model.Teacher;
import org.example.repositoryDAO.CourseTeacherDAO;
import org.example.repositoryDAO.TeacherDAO;
import org.example.repositoryDAO.impl.CourseTeacherDAOImpl;
import org.example.repositoryDAO.impl.TeacherDAOImpl;
import org.example.service.TeacherService;
import org.example.servlet.dto.TeacherIncomingDto;
import org.example.servlet.dto.TeacherOutGoingDto;
import org.example.servlet.dto.TeacherUpdateDto;
import org.example.servlet.mapper.TeacherDtoMapper;
import org.example.servlet.mapper.impl.TeacherDtoMapperImpl;

import java.util.List;
import java.util.Optional;

public class TeacherServiceImpl implements TeacherService {
    private static TeacherService instance;
    private static final TeacherDAO teacherDao = TeacherDAOImpl.getInstance();
    private static  final CourseTeacherDAO courseTeacherDAO = CourseTeacherDAOImpl.getInstance();
    private static final TeacherDtoMapper teacherDtoMapper = TeacherDtoMapperImpl.getInstance();
    
    private TeacherServiceImpl() {
    }
    
    public static synchronized TeacherService getInstance() {
        if (instance == null) {
            instance = new TeacherServiceImpl();
        }
        return instance;
    }

    /**
     * Проверяем наличие записи в таблице по id
     * Если ее нет выбрасываем исключение
     * @param teacherId
     * @throws NotFoundException
     */
    private void checkTeacherExist(Long teacherId) throws NotFoundException {
        if (!teacherDao.existsById(teacherId)) {
            throw new NotFoundException("Teacher not found.");
        }
    }

    /**
     * Преобразуем TeacherIncomingDto в Teacher
     * Возвращаем TeacherOutGoingDto с полученным id
     * @param teacherDto
     * @return
     */
    @Override
    public TeacherOutGoingDto save(TeacherIncomingDto teacherDto) {
        Teacher teacher = teacherDtoMapper.map(teacherDto);
        teacher = teacherDao.save(teacher);
        return teacherDtoMapper.map(teacher);
    }

    /**
     * Проверяем наличие записи по id, если нет выбрасываем исключение
     * Обновляем запись в бд
     * @param teacherDto
     * @throws NotFoundException
     */
    @Override
    public void update(TeacherUpdateDto teacherDto) throws NotFoundException {
        checkTeacherExist(teacherDto.getId());
        if (teacherDto.getCourse() != null) {
            if (teacherDto.getCourse().getName().equals("delete")) {
                Optional<CourseTeacher> courseTeacher = courseTeacherDAO.findByCourseIdAndTeacherId(
                        teacherDto.getCourse().getId(),
                        teacherDto.getId()
                );
                courseTeacher.ifPresent(ct -> courseTeacherDAO.deleteById(ct.getId()));

            } else {
                CourseTeacher courseTeacher = new CourseTeacher(
                        null,
                        teacherDto.getCourse().getId(),
                        teacherDto.getId()
                );
                courseTeacherDAO.save(courseTeacher);
            }
        }
        teacherDao.update(teacherDtoMapper.map(teacherDto));
    }

    /**
     * Получаем учителя по id, если null выбрасываем исключение
     * Возвращаем TeacherOutGoingDto
     * @param teacherId
     * @return
     * @throws NotFoundException
     */
    @Override
    public TeacherOutGoingDto findById(Long teacherId) throws NotFoundException {
        Teacher teacher = teacherDao.findById(teacherId).orElseThrow(() -> 
                new NotFoundException("Teacher not found."));
        return teacherDtoMapper.map(teacher);
    }

    /**
     * Получаем из бд всех Teacher
     * Возвращаем лист TeacherOutGoingDto
     * @return
     */
    @Override
    public List<TeacherOutGoingDto> findAll() {
        List<Teacher> allTeacher = teacherDao.findAll();
        return teacherDtoMapper.map(allTeacher);
    }

    /**
     * Проверяем есть ли запись по id, выбрасываем исключение если нет.
     * Удаляем записб из бд
     * @param teacherId
     * @return
     * @throws NotFoundException
     */
    @Override
    public boolean delete(Long teacherId) throws NotFoundException {
        checkTeacherExist(teacherId);
        courseTeacherDAO.deleteByTeacherId(teacherId);
        return teacherDao.deleteById(teacherId);
    }
}
