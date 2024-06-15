package com.project.collegeproject.service;

import java.util.List;
import java.util.Optional;

import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.entity.Admin;

public interface AdminService {
    
    int saveAdmin(Admin admin);
    Optional<Admin> findAdminByEmail(String email);
    Integer findAdminIdByEmail(String email);
    int createAdminIdTable(int id);
    String saveTest(String table, AdminTable adminTable);
    void createTestTable(String testid);
    List<AdminTable> getAdminTestData(int id);
    String getQeustionById(int adminId, int id);  // Corrected method name
    List<TestTable> findStudentByTest(String testId);
    String verifyToken(String token);
    Optional<AdminTable> getAdminTableByTestId(String tesId);
    public String getNameByEmail(String email);

}
