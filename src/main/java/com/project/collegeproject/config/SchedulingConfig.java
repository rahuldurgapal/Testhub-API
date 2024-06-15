package com.project.collegeproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.collegeproject.service.impl.AdminServiceImpl;
import com.project.collegeproject.service.impl.StudentServiceImpl;

@Configuration
@EnableScheduling
@Component
public class SchedulingConfig {
    
    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Scheduled(fixedRate = 120000) //Runs every 60 seconds
    public void checkAndDeleteExpiredAdmin() {
        adminServiceImpl.deleteExpiredAdmin();
        studentServiceImpl.deleteExpiredStudent();
        
    }
}

