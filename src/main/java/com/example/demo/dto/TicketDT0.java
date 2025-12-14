package com.example.demo.dto;

import com.example.demo.entity.Ticket;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class TicketDT0 {
    private  String from;
    private  String to;
    private String type;


    private LocalDate date;

    public TicketDT0(String from, LocalDate date, String to, String type) {
        this.from = from;
        this.date = date;
        this.to = to;
        this.type = type;
    }

    public TicketDT0(Ticket ticket) {
        this.date = ticket.getDate();
        this.from=ticket.getFromCity();
        this.to=ticket.getToCity();
        this.type=ticket.getType();
    }

    public TicketDT0() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


