package com.shashank.kafkarabbit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shashank.kafkarabbit.model.NotificationRequest;
import com.shashank.kafkarabbit.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public String sendNotification(@RequestBody NotificationRequest request) throws JsonProcessingException {
        notificationService.sendNotification(request);
        return "Notification request sent to Kafka";
    }
}