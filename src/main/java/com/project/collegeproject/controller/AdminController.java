package com.project.collegeproject.controller;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.IdRequestDto;
import com.project.collegeproject.dto.Question;
import com.project.collegeproject.dto.TestDetailsDto;
import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.entity.Admin;
import com.project.collegeproject.service.AdminService;
import com.project.collegeproject.service.QuestionService;
import com.project.collegeproject.service.TestDetailsTransfom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestDetailsTransfom testDetailTransform;


    //admin registration handler and create adminId table
    @PostMapping("/auth/signup")
    public ResponseEntity<String> saveAdmin(@RequestBody Admin admin) throws Exception{
        
      Optional<Admin> adminOptional = adminService.findAdminByEmail(admin.getEmail());
      if (adminOptional.isPresent()) {
          return new ResponseEntity<>("Admin with this email already exists.", HttpStatus.CONFLICT);
      }
     if(adminService.saveAdmin(admin)==1) {
            return ResponseEntity.ok("Registration Successfull. Check Yur emial for verification link");
        }

       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong in server");
    }

    

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
      String result = adminService.verifyToken(token);
      if(result.equals("Email verified Successfully")) {
         return new ResponseEntity<>(result,HttpStatus.OK);
      } else {
         return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
      }
    }

    @PostMapping("/auth/login")
public ResponseEntity<?> loginAdmin(@RequestBody Admin admin, HttpServletRequest request) {
    // Attempt to find the admin by email
    Optional<Admin> optionalAdmin = adminService.findAdminByEmail(admin.getEmail());
    
    // Check if admin is present
    if (optionalAdmin.isPresent()) {
        Admin adm = optionalAdmin.get();

        if(!adm.isEnabled())
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please verify email first..");
        
        // Validate the password
        if (adm.getPassword().equals(admin.getPassword())) {

            HttpSession session = request.getSession(true);
                session.setAttribute("loggedInAdmin", adm);
            return ResponseEntity.ok(adm);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email and password");
        }
    } else {
        // Admin not found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email and password");
    }
}
  
    //save test and create test_id table
    @PostMapping("/organized-test")
    public ResponseEntity<String> saveTest(@RequestParam("table_id") String id,
                                           @RequestParam("test_name") String name,
                                           @RequestParam("csv_file") MultipartFile file,
                                           @RequestParam("time") String time,
                                           @RequestParam("date") LocalDate date,
                                           HttpSession session) {

                    // Check if admin is logged in
                    if (session.getAttribute("loggedInAdmin") == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                    }

        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            List<Question> questions = questionService.parseCSV(reader);

            AdminTable adminTable = new AdminTable();
            adminTable.setTime(time);
            adminTable.setDate(date);
            adminTable.setQuestions(questions);
            adminTable.setTestName(name);

            String testId = adminService.saveTest(id, adminTable);
            return ResponseEntity.ok("test_id: " + testId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving test data");
        }
    }
    
    @GetMapping("/test-list")
    public ResponseEntity<TestDetailsDto> getAllTest(@RequestBody() IdRequestDto id, HttpSession session) {

        if (session.getAttribute("loggedInAdmin") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    	List<AdminTable> test = adminService.getAdminTestData(id.getId());
        System.out.println(test);
    	if(test==null) {
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    	}

        // Transform to the desired format
        TestDetailsDto testDetailsDTO = testDetailTransform.transform(test);

        return ResponseEntity.ok(testDetailsDTO);

    }
    
    @GetMapping("/question/{admin_id}/{id}")
    public ResponseEntity<String> getQuestion(@PathVariable("admin_id") int adminId, @PathVariable("id") int id) {
    	
    	String question = adminService.getQeustionById(adminId, id);
    	if(question==null) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong in server to find question");
    	}
    	return ResponseEntity.ok(question);
    }
    
    @GetMapping("/getStudent")
    public ResponseEntity<List<TestTable>> getStudentByTest(@RequestParam("test_id") String id, HttpSession session) {
    	if (session.getAttribute("loggedInAdmin") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    	List<TestTable> testTable = adminService.findStudentByTest(id);
    	if(testTable==null) {
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    	}
    	for(TestTable table: testTable) {
            String name = adminService.getNameByEmail(table.getApplied_student());
            table.setName(name);
        }
    	return ResponseEntity.ok(testTable);
    }

    
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logoutAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("loggedInAdmin");
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}
