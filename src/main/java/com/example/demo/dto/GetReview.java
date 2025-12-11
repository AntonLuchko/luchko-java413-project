package com.example.demo.dto;

import com.example.demo.entity.Reviews;

import java.time.LocalDate;

public class GetReview {
    private String text;
    private LocalDate date;
    private String userName;

    public GetReview() {
    }
    public GetReview(Reviews reviews) {
        this.text = reviews.getText();
        this.date = reviews.getDate();
        this.userName=reviews.getUser().getRealUsername();
    }

    public GetReview(String text, LocalDate date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
