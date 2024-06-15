package com.project.collegeproject.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.entity.Student;
import com.project.collegeproject.mapper.StudentMapper;



@Repository
public class StudentRepository {

   @Autowired
   private JdbcTemplate jdbcTemplate;
   


    public void saveStudent(Student student, String testId) {
           LocalDateTime localDateTime = LocalDateTime.now();
           jdbcTemplate.update(
                 "insert into student(name, email, resume_link, enabled, join_date) values (?,?,?,?,?)",
                 student.getName(),student.getEmail(), student.getResume(),false, localDateTime);
           
    }

    public void saveStudentInTestTable(String testId,String email) {
      String table = "test_"+testId;
      String sql= "insert into "+table+ "(applied_student,result,status)values(?,?,?)";
      jdbcTemplate.update(sql, email,"0","not attend");
    }

    public Optional<Student> findStudentByEmail(String email) {
      String sql = "select * from student where email = ?";
      try {
            List<Student> student = jdbcTemplate.query(sql,new StudentMapper(),email);
            if(student.isEmpty()) {
                  return Optional.empty();
            } else {
                  return Optional.of(student.get(0));
            }
      } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
      }
    }

    public boolean isTableExsist(String id) {
      String table = "test_"+id;
      String sql = "SHOW TABLES LIKE ?";
      try{
         return jdbcTemplate.queryForObject(sql,String.class,table)!=null;
      }catch(Exception e) {
            return false;
      }
    }

	public void saveTest(TestTable testTable) {
            String table = "test_"+testTable.getTestId();

            String sql = "update "+table+ " set result=?, status=?,response=? where applied_student = ?";
            try{
             jdbcTemplate.update(sql,testTable.getResult(),"attend",testTable.getResponse(),testTable.getApplied_student());
        }  catch(Exception e) {
            e.printStackTrace();
               }
      }

      public void suspendTest(TestTable testTable) {
            String table = "test_"+testTable.getTestId();

            String sql = "update "+table+ " set result=?, status=?,response=? where applied_student = ?";
            try{
             jdbcTemplate.update(sql,testTable.getResult(),testTable.getStatus(),testTable.getResponse(),testTable.getApplied_student());
        }  catch(Exception e) {
            e.printStackTrace();
               }
      }

      public boolean checkStudent(String testId,String email) {
            String table = "test_"+testId;
            String sql = "SELECT COUNT(*) FROM " + table + " WHERE applied_student = ?";

             try {
                    Integer count = jdbcTemplate.queryForObject(sql, Integer.class,email);
                    System.out.println("count= "+count);
                    return count != null && count > 0;
                } catch (DataAccessException e) {
                	System.out.println("catch exception in verifyTestId() method");
                    e.printStackTrace();
                    return false;
                }

      }
}
