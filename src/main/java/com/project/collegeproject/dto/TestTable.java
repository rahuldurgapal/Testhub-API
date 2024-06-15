package com.project.collegeproject.dto;

public class TestTable {

	 private int id;
	 private String testId;
	 private String applied_student;
	 private String result;
	 private String status;
	 private String response;

	 

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApplied_student() {
		return applied_student;
	}
	public void setApplied_student(String applied_student) {
		this.applied_student = applied_student;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	

	@Override
	public String toString() {
		return "TestTable [id=" + id + ", testId=" + testId + ", applied_student=" + applied_student + ", result="
				+ result + ", status=" + status + ", response=" + response + "]";
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	 
	 
}
