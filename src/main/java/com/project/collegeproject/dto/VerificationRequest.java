package com.project.collegeproject.dto;

public class VerificationRequest {
    
    String email;
    String testId;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTestId() {
        return testId;
    }
    public void setTestId(String testId) {
        this.testId = testId;
    }
    @Override
    public String toString() {
        return "VerificationRequest [email=" + email + ", testId=" + testId + "]";
    }

    
}
