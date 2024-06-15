package com.project.collegeproject.entity;

import java.time.LocalDate;

public class Admin {
    
    private int id;
    private String email;
    private String name;
    private boolean enabled;
    private LocalDate registrationDate;
    
   
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    private String password;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    @Override
    public String toString() {
        return "Admin [id=" + id + ", email=" + email + ", name=" + name + ", enabled=" + enabled
                + ", registrationDate=" + registrationDate + ", password=" + password + "]";
    }
    

    

}
