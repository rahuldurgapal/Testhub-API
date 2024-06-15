package com.project.collegeproject.dto;

import java.time.LocalDate;
import java.util.List;



public class AdminTable {
 
	 private int id;
	 private String testName;
	 private String testId;
	 private List<Question> questions;
	 private String time;
	 private LocalDate date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "AdminTable [id=" + id + ", testName=" + testName + ", testId=" + testId + ", qustions=" + questions
				+ ", time=" + time + ", date=" + date + "]";
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	 
	 
}
