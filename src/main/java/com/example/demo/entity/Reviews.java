package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "reviews")
public class Reviews {

    public Reviews() {
    }

    public Reviews( Users user, String text, boolean status, LocalDate date, boolean active) {
        this.user = user;
        this.text = text;
        this.status = status;
        this.date = date;
        this.active = active;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private boolean status=false;

    @Column(nullable = false)
    private boolean active=true;

    private LocalDate date=LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
