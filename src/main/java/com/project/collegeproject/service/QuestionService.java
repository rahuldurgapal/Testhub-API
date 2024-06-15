package com.project.collegeproject.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.project.collegeproject.dto.Question;

@Service
public class QuestionService {
    
    public List<Question> parseCSV(InputStreamReader reader) throws Exception {
        
        List<Question> questions = new ArrayList<>();
        try(CSVReader csvReader = new CSVReader(reader)) {
            
            List<String[]> rows = csvReader.readAll();
            int id =1;

            for(String[] row: rows) {
                Question question = new Question();
                question.setId(id++);
                question.setQuestion(row[0]);
                question.setAnswer(row[5]);
                question.setOptions(Arrays.asList(row[1],row[2],row[3],row[4]));
                
                questions.add(question);
            }
        }

        return questions;
    }
}
