package com.project.collegeproject.config;

import java.util.UUID;


public class AdminConfig {
    
    public static String generateId() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", ""); // Remove hyphens
        return uuidStr.substring(0, 6);
    }
    
    public static int getAdminId(String testId) {
    	System.out.println("testId= "+testId);
    	System.out.println("TestId length is= "+testId.length());
    	String temp;
        if(testId.length()==7) {
        	temp = testId.substring(testId.length()-1);
        	return Integer.parseInt(temp);
        	
        } else if(testId.length()==8) {
        	temp = testId.substring(testId.length()-2,testId.length()-1);
        	return Integer.parseInt(temp);
        }else if(testId.length()==9) {
        	temp = testId.substring(testId.length()-3,testId.length()-1);
        	return Integer.parseInt(temp);
        }
        
        return 0;
    }

    public static String getTestid(String token) {
        int index = token.lastIndexOf("-");
        return token.substring(index+1);
    }
    

}
