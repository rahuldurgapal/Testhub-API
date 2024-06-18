package com.project.collegeproject.service.impl;

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
import com.project.collegeproject.entity.Admin;
import com.project.collegeproject.repository.AdminRepository;
import com.project.collegeproject.service.AdminService;
import com.project.collegeproject.service.TokenService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AdminServiceImpl implements AdminService{

   
	@Autowired
    private AdminRepository adminRepository; 

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final long TOKEN_EXPIRY_DURATION=5;

    private Map<String,TokenService> tokenStore = new ConcurrentHashMap<>();

    @Override
    public int saveAdmin(Admin admin) {
        int res = adminRepository.save(admin);
        sendVerificationEmail(admin);
        return res;
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    @Override
    public int createAdminIdTable(int id) {
        return adminRepository.createAdminIdTable(id);
    }

    @Override
    public Integer findAdminIdByEmail(String email) {
       return adminRepository.findAdminIdByEmail(email);
    }

    @Override
	public List<AdminTable> getAdminTestData(int id) {
		return adminRepository.getTestData(id);
	}

    @Override
    public String saveTest(String table, AdminTable adminTable) {
        
        String testId = adminRepository.saveTest(table, adminTable);
        createTestTable(testId);

        return testId;
    }
    
    @Override
   	public void createTestTable(String testid) {
   		 adminRepository.createTestTable(testid);
   		
   	}

	@Override
	public String getQeustionById(int adminId, int id) {
		return adminRepository.getQuestionById(adminId, id);
	}
    

	@Override
    public Optional<AdminTable> getAdminTableByTestId(String tesId) {
        return adminRepository.getAdminTableByTestId(tesId);
    }

    @Override
	public List<TestTable> findStudentByTest(String testId) {
		
		return adminRepository.findStudentByTest(testId);
	}

  public void sendVerificationEmail(Admin admin) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_DURATION);
        // Assuming tokenStore is defined and properly initialized in your class
        tokenStore.put(token, new TokenService(admin.getEmail(), expiryTime));

        String toAddress = admin.getEmail();
        String subject = "Please verify your Email for Registration";
        String url = "http://43.205.112.178:8080/admin/verify?token=" + token;

        String content = "<div style=\"font-family: Arial, sans-serif; text-align: center; color: #333;\">" +
                "<h2 style=\"color: #4CAF50;\">Verify Your Email</h2>" +
                "<p>Dear " + admin.getName() + ",</p>" +
                "<p>Please click the link below to verify your registration:</p>" +
                "<a href=\"" + url + "\" style=\"display: inline-block; padding: 10px 20px; margin: 20px 0; font-size: 16px; color: white; background-color: #4CAF50; text-decoration: none; border-radius: 5px;\">Verify Email</a>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("rahuldurgapal4@gmail.com");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true); // true indicates HTML content

            mailSender.send(message);

            System.out.println("Verification email sent successfully....");
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

   @Override
    public String verifyToken(String token) {
        TokenService tokenServie = tokenStore.get(token);
        if(tokenServie==null) {
            return "Invalid token";
        }

        if(tokenServie.getExpiryTime().isBefore(LocalDateTime.now())) {
            tokenStore.remove(token);
            return "Token Expired";
        }

        int updateCount = jdbcTemplate.update("update admin set enabled = true where email = ?", tokenServie.getEmail());
        int id = findAdminIdByEmail(tokenServie.getEmail());
        if(updateCount>0) {
            createAdminIdTable(id);
            return "Email verified Successfully";
        } else {
            return "User Not Found";
        }
    }

    public void deleteExpiredAdmin() {
        LocalDateTime now = LocalDateTime.now();
        List<String> expiredTokens= tokenStore.entrySet().stream()
                                    .filter(entry -> entry.getValue().getExpiryTime().isBefore(now))
                                    .map(Map.Entry::getKey)
                                    .collect(Collectors.toList());
                
                for(String token: expiredTokens) {
                    String email = tokenStore.get(token).getEmail();
                    jdbcTemplate.update("delete from admin where email = ? and enabled=false",email);
                    tokenStore.remove(token);
                }
                    
    }

    @Override
    public String getNameByEmail(String email) {
        
        return adminRepository.getNameByEmail(email);
    }
    
}