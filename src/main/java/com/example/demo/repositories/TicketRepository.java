package com.example.demo.repositories;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByStatusTrue();

    @Modifying
    @Transactional
    @Query(value = "UPDATE tickets  SET status = 0 WHERE status = 1 AND date < :today", nativeQuery = true)
    int updateExpiredTickets(@Param("today") LocalDate today);


    List<Ticket> findAllByUserAndStatusTrue(Users user);
}
