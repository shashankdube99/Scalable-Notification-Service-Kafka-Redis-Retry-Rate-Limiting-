package com.shashank.kafkarabbit.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shashank.kafkarabbit.model.NotificationRequest;
import com.shashank.kafkarabbit.retry.RetryProcessor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RetryProcessor retryProcessor;

    public NotificationConsumer(RetryProcessor retryProcessor) {
        this.retryProcessor = retryProcessor;
    }

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listen(String message) {
        try {
            NotificationRequest request = objectMapper.readValue(message, NotificationRequest.class);
            System.out.println("🔔 Processing Notification:");
            System.out.println("To: " + request.getTo());
            System.out.println("Message: " + request.getMessage());

            // Simulate failure
            if (request.getMessage().contains("fail")) {
                throw new RuntimeException("Simulated failure");
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to process message: " + e.getMessage());

            try {
                NotificationRequest request = objectMapper.readValue(message, NotificationRequest.class);
                retryProcessor.addForRetry(request, 1);
            } catch (Exception ex) {
                System.err.println("❌ Failed to add to retry queue: " + ex.getMessage());
            }
        }
    }
}