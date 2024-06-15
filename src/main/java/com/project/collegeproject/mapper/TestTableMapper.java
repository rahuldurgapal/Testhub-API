package com.project.collegeproject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.collegeproject.dto.TestTable;

public class TestTableMapper implements RowMapper<TestTable>{

	@Override
	public TestTable mapRow(@SuppressWarnings("null") ResultSet rs, int arg) throws SQLException {
		TestTable testTable = new TestTable();
		testTable.setId(rs.getInt("id"));
		testTable.setApplied_student(rs.getString("applied_student"));
		testTable.setResult(rs.getString("result"));
		testTable.setStatus(rs.getString("status"));
		return testTable;
	}

}
