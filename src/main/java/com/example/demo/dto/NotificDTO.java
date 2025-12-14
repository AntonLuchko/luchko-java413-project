package com.example.demo.dto;

import com.example.demo.entity.Notifications;

public class NotificDTO {
    private String message;
    private boolean active;
    private long id;

    public NotificDTO() {
    }

    public NotificDTO(Notifications notifications) {
        this.message = notifications.getMessage();
        this.active=notifications.isActive();
        this.id=notifications.getId();
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
