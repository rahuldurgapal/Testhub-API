package com.project.collegeproject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.project.collegeproject.entity.Admin;

public class AdminMapper implements RowMapper<Admin> {
    @Override
    public Admin mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("id"));
        admin.setName(rs.getString("name"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        admin.setEnabled(rs.getBoolean("enabled"));
        admin.setRegistrationDate(rs.getDate("join_date").toLocalDate());
        return admin;
    }
    
}