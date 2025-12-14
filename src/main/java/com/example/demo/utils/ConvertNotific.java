package com.example.demo.utils;

import com.example.demo.dto.NotificDTO;
import com.example.demo.entity.Notifications;
import org.springframework.stereotype.Component;


import javax.management.Notification;
 @Component
public class ConvertNotific {
    public NotificDTO convert(Notifications notification) {
        return new NotificDTO(notification);
    }
}
