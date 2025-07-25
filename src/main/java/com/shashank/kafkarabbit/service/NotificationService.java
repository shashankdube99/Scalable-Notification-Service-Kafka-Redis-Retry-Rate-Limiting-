package com.shashank.kafkarabbit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shashank.kafkarabbit.model.NotificationRequest;
import com.shashank.kafkarabbit.ratelimiter.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final String TOPIC = "notifications";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RateLimiterService rateLimiter;

    public void sendNotification(NotificationRequest request) throws JsonProcessingException {
        String recipient = request.getTo();  // ✅ Fixed: using correct field name

        if (!rateLimiter.isAllowed(recipient)) {
            throw new RuntimeException("❌ Rate limit exceeded for: " + recipient);
        }

        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaTemplate.send(TOPIC, jsonMessage);
        System.out.println("🔔 Notification sent to Kafka for: " + recipient);
    }
}