package com.project.collegeproject.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.Question;

public class AdminTableMapper implements RowMapper<AdminTable> {

	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public AdminTable mapRow(@SuppressWarnings("null") ResultSet rs, int arg) throws SQLException {
	AdminTable adminTable = new AdminTable();
        adminTable.setId(rs.getInt("id"));
        adminTable.setTestName(rs.getString("test_name"));
        adminTable.setTestId(rs.getString("test_id"));
        String questionsJson = rs.getString("test_questions");
        try {
            List<Question> questions = objectMapper.readValue(questionsJson, new TypeReference<List<Question>>() {});
            adminTable.setQuestions(questions);
        } catch (Exception e) {
            throw new SQLException("Failed to parse test_questions JSON", e);
        }
        adminTable.setTime(rs.getString("time"));
        Date date = rs.getDate("test_date");
        adminTable.setDate(date.toLocalDate());
        return adminTable;
	}
 
}
