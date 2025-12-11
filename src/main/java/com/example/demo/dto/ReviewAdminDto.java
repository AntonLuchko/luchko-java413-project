package com.example.demo.dto;

import com.example.demo.entity.Reviews;

public class ReviewAdminDto {
    private Long reviewId;
    private Long userId;
    private String text;

    public ReviewAdminDto(Reviews reviews) {
        this.text = reviews.getText();
        this.reviewId = reviews.getId();
        this.userId = reviews.getUser().getId();
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
