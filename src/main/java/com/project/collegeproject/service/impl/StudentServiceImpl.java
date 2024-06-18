package com.project.collegeproject.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.entity.Student;
import com.project.collegeproject.repository.StudentRepository;
import com.project.collegeproject.service.AdminService;
import com.project.collegeproject.service.StudentService;
import com.project.collegeproject.service.TokenService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class StudentServiceImpl implements StudentService {
  
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JdbcTemplate jdbcTemplate;

     @Autowired
   private AdminService adminService;

    private static final long TOKEN_EXPIRY_DURATION=5;
    
    private Map<String, TokenService> tokenStore = new ConcurrentHashMap<>();

    @Override
    public void saveStudent(String testId, Student student) {
        studentRepository.saveStudent(student,testId);
        sendVerificationMail(student,testId);
    }

    @Override
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findStudentByEmail(email);
    }

    @Override
    public boolean isTableExsist(String id) {
        return studentRepository.isTableExsist(id);
    }

    public void sendVerificationMail(Student student, String testId) {
        String token = UUID.randomUUID().toString() + "-" + testId;
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_DURATION);
        tokenStore.put(token, new TokenService(student.getEmail(), expiryTime));

        String toAddress = student.getEmail();
        String subject = "Please verify your email for registration";
        String url = "http://43.205.112.178:8080/auth/verify?token=" + token;

        String content = "<div style=\"font-family: Arial, sans-serif; text-align: center; color: #333;\">" +
                "<h2 style=\"color: #4CAF50;\">Hello " + student.getName() + ",</h2>" +
                "<p>Please click the link below to verify your registration:</p>" +
                "<a href=\"" + url + "\" style=\"display: inline-block; padding: 10px 20px; margin: 20px 0; font-size: 16px; color: white; background-color: #4CAF50; text-decoration: none; border-radius: 5px;\">LOGIN Page</a>" +
                "<p>Keep this Test ID: <strong>" + testId + "</strong> for future reference. You will need to enter your email and this Test ID to log in for the test. The test will be available from 12:00 AM to 11:59 PM on the scheduled date.</p>" +
                "<p>Best regards,<br/>TestHub</p>" +
                "</div>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("rahuldurgapal37@gmail.com");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true); // true indicates HTML content

            javaMailSender.send(message);

            System.out.println("Verification email sent successfully....");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }


    @Override
    public String verifyStudentToken(String token, String testId) {
        TokenService tokenService = tokenStore.get(token);
        String email = tokenStore.get(token).getEmail();
        if(tokenService==null) {
            return "Invalid Token";
        }

        if(tokenService.getExpiryTime().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        int count = jdbcTemplate.update("update student set enabled=true where email = ?",tokenService.getEmail());
        if(count>0) {
            studentRepository.saveStudentInTestTable(testId,email);
            return "Email Verified Successfully";
        } else {
            return "User not found";
        }
    }

    public void deleteExpiredStudent() {
        LocalDateTime time = LocalDateTime.now();
        List<String> expiredTokens = tokenStore.entrySet().stream()
                                     .filter(entry -> entry.getValue().getExpiryTime().isBefore(time))
                                     .map(Map.Entry::getKey)
                                     .collect(Collectors.toList());
                    
                    for(String token: expiredTokens) {
                        String email = tokenStore.get(token).getEmail();
                        jdbcTemplate.update("delete from student where email = ? and enabled=false", email);
                        tokenStore.remove(token);
                    }
    }
    
     @Override
    public boolean checkDateExsist(String testId) {
      
      Optional<AdminTable> admintable = adminService.getAdminTableByTestId(testId);
      AdminTable table = admintable.get();
      LocalDate date = table.getDate();

      if(date.isBefore(LocalDate.now())) {
            return false;
      }
      return true;
    }

    @Override
    public void saveTest(TestTable testTable) {
        
        studentRepository.saveTest(testTable);
    }

    @Override
    public boolean checkStudent(String testId, String email) {
        return studentRepository.checkStudent(testId, email);
    }

    @Override
    public void suspendTest(TestTable testTable) {
       studentRepository.suspendTest(testTable); 
    }

   
    
    
}
