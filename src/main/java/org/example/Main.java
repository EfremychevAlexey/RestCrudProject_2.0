package org.example;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.NotFoundException;
import org.example.model.Course;
import org.example.model.Student;
import org.example.repositoryDAO.CourseDAO;
import org.example.repositoryDAO.StudentDAO;
import org.example.repositoryDAO.impl.CourseDAOImpl;
import org.example.repositoryDAO.impl.StudentDAOImpl;
import org.example.service.CourseService;
import org.example.service.StudentService;
import org.example.service.impl.CourseServiceImpl;
import org.example.service.impl.StudentServiceImpl;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.CourseDtoMapper;
import org.example.servlet.mapper.StudentDtoMapper;
import org.example.servlet.mapper.impl.CourseDtoMapperImpl;
import org.example.servlet.mapper.impl.StudentDtoMapperImpl;
import org.example.util.DBInit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, InterruptedException, NotFoundException {

        ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
        DBInit.init(connectionManager);
    }
}