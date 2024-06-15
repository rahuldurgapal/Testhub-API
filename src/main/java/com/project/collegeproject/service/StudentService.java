package com.project.collegeproject.service;

import java.util.Optional;

import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.entity.Student;

public interface StudentService {
 
    public void saveStudent(String testId, Student student);
    public Optional<Student> getStudentByEmail(String email);
    public boolean isTableExsist(String id);
    public String verifyStudentToken(String token, String testId);
    public boolean checkDateExsist(String testId);
    public void saveTest(TestTable testTable);
    public boolean checkStudent(String testId, String email);
    
} 