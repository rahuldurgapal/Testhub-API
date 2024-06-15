package com.project.collegeproject.repository;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.collegeproject.config.AdminConfig;
import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.entity.Admin;
import com.project.collegeproject.mapper.AdminMapper;
import com.project.collegeproject.mapper.AdminTableMapper;
import com.project.collegeproject.mapper.TestTableMapper;

@Repository
public class AdminRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

        public int save(Admin admin) {  
            LocalDateTime registrationDate = LocalDateTime.now();
            return jdbcTemplate.update(
                        "insert into admin (name, email, password, enabled, join_date) values (?,?,?,?,?)",
                        admin.getName(),admin.getEmail(),admin.getPassword(),false, Timestamp.valueOf(registrationDate));
        }

        public Optional<Admin> findAdminByEmail(String email) {
            String sql = "SELECT * FROM admin WHERE email = ?";
            try {
                List<Admin> admins = jdbcTemplate.query(sql, new AdminMapper(), email);
                if (admins.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(admins.get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }

        public Integer findAdminIdByEmail(String email) {
            return jdbcTemplate.queryForObject("select id from admin where email = ?",
                                                 Integer.class, email);
        }


            public int createAdminIdTable(int id) {
                String table = "admin_" + id;
                System.out.println(table);
                String sql = "CREATE TABLE " + table + " (" +
                             "id INT PRIMARY KEY AUTO_INCREMENT, " +
                             "test_name VARCHAR(100) NOT NULL, " +
                             "test_id VARCHAR(50) NOT NULL, " +
                             "test_questions JSON NOT NULL, " +
                             "time VARCHAR(50) NOT NULL, " +
                             "test_date DATE NOT NULL)";
                
                try {
                    jdbcTemplate.execute(sql);
                    return 1; // Indicating successful table creation
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0; // Indicating failure to create the table
                }
            }
            
        public String saveTest(String id,AdminTable adminTable) {

            String testId = AdminConfig.generateId()+id;
            adminTable.setTestId(testId);
            String table = "admin_"+id;
            try{
            
            ObjectMapper objectMapper = new ObjectMapper();
            String questions = objectMapper.writeValueAsString(adminTable.getQuestions());
            String sql = "insert into "+table+ " (test_name, test_id, test_questions, time, test_date) values(?,?,?,?,?)";
            jdbcTemplate.update(sql,adminTable.getTestName(),adminTable.getTestId(),questions,adminTable.getTime(),adminTable.getDate());
            }catch(Exception e) {
                e.printStackTrace();
                return "Failed to save data";
            }
            return testId;
        }
        
        public void createTestTable(String testId) {
        	String table = "test_"+testId;
        	String sql = "create table "+table+ " (id int primary key auto_increment, applied_student varchar(100) not null,"
        			+ "result varchar(100), status varchar(50), response json)";
        	jdbcTemplate.update(sql);
        }
        
        public List<AdminTable> getTestData(int id) {
        	String table = "admin_"+id;
            String sql = "select * from "+table;
            try {
            return jdbcTemplate.query(sql, new AdminTableMapper());
            }catch(Exception e) {
            	return null;
            }
        	
        }
        
        public String getQuestionById(int admin_id, int id) {
        	
        	String table = "admin_"+admin_id;
        	String sql = "select test_questions from "+table+ " where id = ?";
        	try {
        	return jdbcTemplate.queryForObject(sql, String.class,id);
        	}catch(Exception e) {
        	
        		return null;
        	}
        }
        
        public List<TestTable> findStudentByTest(String testId) {
        	
        	String table = "test_"+testId;
        	System.out.println(table);
        	if(verifyTestId(testId)) {
        		String sql = "select * from "+table;
        		try {
        			return jdbcTemplate.query(sql, new TestTableMapper());
        		}catch(Exception e) {
        			System.out.println("Something wrong in findStudnetByTest() method");
        			e.printStackTrace();
        			return null;
        		}
        	}
        	
        	return null;
        }
        
        
        public boolean verifyTestId(String testId) {
            int id = AdminConfig.getAdminId(testId);  

            if (id != 0) {
                String table = "admin_" + id;
                String sql = "SELECT COUNT(*) FROM " + table + " WHERE test_id = ?";
                
                try {
                    Integer count = jdbcTemplate.queryForObject(sql, Integer.class,testId);
                    System.out.println("count= "+count);
                    return count != null && count > 0;
                } catch (DataAccessException e) {
                	System.out.println("catch exception in verifyTestId() method");
                    e.printStackTrace();
                    return false;
                }
            }
            System.out.println("id= "+id);
            return false;
        }
        
        public Optional<AdminTable> getAdminTableByTestId(String testid) {
            int id = AdminConfig.getAdminId(testid);
            String table = "admin_"+id;

            String sql = "select * from "+table +" where test_id = ?";
      try {
            List<AdminTable> adimtable = jdbcTemplate.query(sql,new AdminTableMapper(),testid);
            if(adimtable.isEmpty()) {
                  return Optional.empty();
            } else {
                  return Optional.of(adimtable.get(0));
            }
      } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
      }

        }
        public String getNameByEmail(String email) {
            String sql = "select name from student where email = ?";

            String name = jdbcTemplate.queryForObject(sql,String.class,email);
            return name;

      } 

    }
