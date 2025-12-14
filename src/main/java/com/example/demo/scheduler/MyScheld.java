package com.example.demo.scheduler;

import com.example.demo.services.ScheduledService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class MyScheld {
private final ScheduledService scheduledService;

    public MyScheld(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }

    @Scheduled(cron = "${myCron}")
    public void delNoActiveNotifications() {
        scheduledService.deleteNoActiveNotifications();
    }


    @Scheduled(cron = "${myCron}")
    public void notificTicket() {
        scheduledService.notificTicket();
    }

    @Scheduled(cron = "${myCron}")
    public void noStatusTicket() {
        scheduledService.noStatusTicket();
    }
}
