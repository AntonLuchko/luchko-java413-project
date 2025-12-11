package com.example.demo.dto;

public class EmailBlocked {
    String email;
    String resone;
    public String getEmail() {
        return email;
    }

    public EmailBlocked(String email, String resone) {
        this.email = email;
        this.resone = resone;
    }

    public EmailBlocked() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResone() {
        return resone;
    }

    public void setResone(String resone) {
        this.resone = resone;
    }
}
