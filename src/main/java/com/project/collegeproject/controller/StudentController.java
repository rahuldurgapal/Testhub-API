package com.project.collegeproject.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.collegeproject.config.AdminConfig;
import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.SuspendDto;
import com.project.collegeproject.dto.TestTable;
import com.project.collegeproject.dto.VerificationRequest;
import com.project.collegeproject.entity.Student;
import com.project.collegeproject.service.AdminService;
import com.project.collegeproject.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
// @RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

     @PostMapping("/auth/signup")
     public ResponseEntity<String> saveStudent(@RequestParam("test_id") String testId, @RequestBody Student student) {
        
          boolean check = studentService.isTableExsist(testId);
          if(!check) {
               return new ResponseEntity<>("Test_id is not correct. ",HttpStatus.CONFLICT);
          }

          boolean checkDate = studentService.checkDateExsist(testId);
          if(!checkDate) {
               return new ResponseEntity<>("Date Expired ",HttpStatus.NOT_ACCEPTABLE);
          }

          boolean std = studentService.checkStudent(testId,student.getEmail());
          if(std) {
               return new ResponseEntity<>("Email is already exsist. ",HttpStatus.UNAUTHORIZED);
          }
          studentService.saveStudent(testId, student);
          return ResponseEntity.ok("Please Verify You Email...");
     }

     @GetMapping("/auth/verify")
     public ResponseEntity<String> verifyStudentToken(@RequestParam("token") String token) {
          String testId = AdminConfig.getTestid(token);
          String result = studentService.verifyStudentToken(token,testId);
          if(result.equals("Email Verified Successfully")) {
               return new ResponseEntity<>(result,HttpStatus.OK);
          } else {
               return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
          }
     }


     @PostMapping("/auth/login")
       public ResponseEntity<?> loginStudent(@RequestBody VerificationRequest request,HttpServletRequest req) {
            String email = request.getEmail();
            String testId = request.getTestId();
                   
            Optional<Student> std = studentService.getStudentByEmail(email);
          if(std.isPresent()) {
               if(!std.get().isEnabled()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email not verified");
               }

               if(studentService.isTableExsist(testId)) {

                    if(!studentService.checkDateExsist(testId)) {
                         return new ResponseEntity<>("Date Expired ",HttpStatus.NOT_ACCEPTABLE);
                    }
                   AdminTable adminTable = adminService.getAdminTableByTestId(testId).get();
                    HttpSession session = req.getSession(true);
                session.setAttribute("loggedInStudent",adminTable);
                  return ResponseEntity.ok(adminTable);
               } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("incorrect test id");
               }
          } 

          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email is not exsist");
            
       }


      @PutMapping("/auth/submit-response")
      public ResponseEntity<?> saveTest(@RequestBody TestTable testTable, HttpServletRequest request) {
          HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInStudent") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
          studentService.saveTest(testTable);
          return new ResponseEntity<>("Test save successfully",HttpStatus.OK);
      }
     
      @GetMapping("/auth/suspend")
      public ResponseEntity<?> suspendStudent(SuspendDto sus, HttpServletRequest request) {

          HttpSession session = request.getSession(false);
          if (session == null || session.getAttribute("loggedInStudent") == null) {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
          }
          TestTable testTable = new TestTable();
          testTable.setStatus("suspend");
          testTable.setResult("0");
          testTable.setTestId(sus.getTestId());
          testTable.setApplied_student(sus.getEmail());
          return new ResponseEntity<>("Response submitted successfully",HttpStatus.OK);
      }

      @PostMapping("/auth/logout")
      public ResponseEntity<String> logoutStudent(HttpServletRequest request) {
          HttpSession session = request.getSession(false);
          if (session != null) {
              session.removeAttribute("loggedInStudent");
              session.invalidate();
          }
          return ResponseEntity.ok("Logged out successfully");
      }
     }