package com.example.demo.utils;

import com.example.demo.dto.NotificDTO;
import com.example.demo.dto.TicketDT0;
import com.example.demo.entity.Notifications;
import com.example.demo.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class ConvertrTicket {
    public TicketDT0 convert(Ticket ticket) {
        return new TicketDT0(ticket);
    }
}
