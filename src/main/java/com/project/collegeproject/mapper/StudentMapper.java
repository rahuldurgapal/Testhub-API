package com.project.collegeproject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.project.collegeproject.entity.Student;

public class StudentMapper implements RowMapper<Student>{

    @Override
    public Student mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setResume(rs.getString("resume_link"));
        student.setEnabled(rs.getBoolean("enabled"));
        student.setRegistrationDate(rs.getDate("join_date").toLocalDate());

        return student;
    }
    
}
