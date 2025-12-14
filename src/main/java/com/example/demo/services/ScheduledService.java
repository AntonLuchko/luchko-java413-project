package com.example.demo.services;

import com.example.demo.entity.Notifications;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.Users;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.TicketRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledService {

    private final NotificationRepository notificationRepository;
    private final TicketRepository ticketRepository;


    public ScheduledService(NotificationRepository notificationRepository, TicketRepository ticketRepository) {
        this.notificationRepository = notificationRepository;
        this.ticketRepository = ticketRepository;

    }

     public void deleteNoActiveNotifications() {
        notificationRepository.deleteNoActive();
    }

    public void notificTicket(){
        List<Ticket> tickets = ticketRepository.findAllByStatusTrue();
        for (Ticket ticket : tickets) {
            Users user=ticket.getUser();
            Notifications  notifications = new Notifications();
            notifications.setMessage("На " + ticket.getDate() + "у вас запланирована поездка!");
            notifications.setUser(user);
            notificationRepository.save(notifications);
        }
    }

    public void  noStatusTicket(){
        ticketRepository.updateExpiredTickets(LocalDate.now());
    }
}
