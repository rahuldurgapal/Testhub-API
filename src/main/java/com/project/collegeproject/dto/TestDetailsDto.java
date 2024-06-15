package com.project.collegeproject.dto;

import java.util.List;

public class TestDetailsDto {
    
    private List<Integer> test_id;
    private List<String> test_name;
    private List<String> test_date;
    public List<Integer> getTest_id() {
        return test_id;
    }
    public void setTest_id(List<Integer> test_id) {
        this.test_id = test_id;
    }
    public List<String> getTest_name() {
        return test_name;
    }
    public void setTest_name(List<String> test_name) {
        this.test_name = test_name;
    }
    public List<String> getTest_date() {
        return test_date;
    }
    public void setTest_date(List<String> test_date) {
        this.test_date = test_date;
    }

    
}
