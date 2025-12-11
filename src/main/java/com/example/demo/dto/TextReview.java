package com.example.demo.dto;

public class TextReview {
    private String text;

    public TextReview() {
    }

    public TextReview(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
