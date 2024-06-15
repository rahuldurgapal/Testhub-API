package com.project.collegeproject.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.collegeproject.dto.AdminTable;
import com.project.collegeproject.dto.TestDetailsDto;

@Service
public class TestDetailsTransfom {
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

public TestDetailsDto transform(List<AdminTable> adminTable) {
        TestDetailsDto testDetailsDTO = new TestDetailsDto();

        List<String> testIds = new ArrayList<>();
        List<String> testNames = new ArrayList<>();
        List<String> testDates = new ArrayList<>();

        for (AdminTable detail : adminTable) {
            testIds.add(detail.getTestId());
            testNames.add(detail.getTestName());
            testDates.add(detail.getDate().format(formatter));

        }

        testDetailsDTO.setTest_id(testIds);
        testDetailsDTO.setTest_name(testNames);
        testDetailsDTO.setTest_date(testDates);

        return testDetailsDTO;
    }
}
