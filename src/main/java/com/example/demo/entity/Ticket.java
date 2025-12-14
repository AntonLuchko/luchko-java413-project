package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_city", nullable = false)
    private String fromCity;

    @Column(name = "destination_city", nullable = false)
    private String toCity;

    @Column(name = "travel_date", nullable = false)
    private LocalDate date;

    @Column(name = "transport_type", nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    private boolean status=true;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Конструкторы
    public Ticket() {
    }

    public Ticket(String fromCity, String toCity, LocalDate date, String type) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.date = date;
        this.type = type;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getFromCity() {
        return fromCity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
