package com.shashank.kafkarabbit.retry;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shashank.kafkarabbit.model.NotificationRequest;
import com.shashank.kafkarabbit.model.RetryNotification;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RetryProcessor {

    private final Queue<RetryNotification> retryQueue = new ConcurrentLinkedQueue<>();

    public void addForRetry(NotificationRequest request, int attempts) {
        long backoff = (long) Math.pow(2, attempts) * 1000; // Exponential backoff
        retryQueue.offer(new RetryNotification(request, attempts, backoff));
        System.out.println("🔁 Added to retry queue (attempt " + attempts + ")");
    }

    @Scheduled(fixedDelay = 2000)
    public void processRetries() {
        long now = System.currentTimeMillis();

        retryQueue.removeIf(retry -> {
            if (retry.getNextRetryTimeMillis() <= now) {
                try {
                    // Simulate retrying the notification
                    System.out.println("🔄 Retrying notification: " + retry.getRequest().getTo());

                    // Simulate failure on purpose (optional for testing)
                    if (retry.getAttempts() < 2) {
                        throw new RuntimeException("Simulated failure");
                    }

                    System.out.println("✅ Retry succeeded!");
                    return true; // remove from queue

                } catch (Exception e) {
                    if (retry.getAttempts() >= 3) {
                        System.out.println("❌ Giving up after 3 attempts: " + retry.getRequest().getTo());
                        return true; // stop retrying
                    } else {
                        retry.incrementAttempts((long) Math.pow(2, retry.getAttempts()) * 1000);
                        retryQueue.offer(retry); // re-add for future attempt
                        return true; // remove current instance
                    }
                }
            }
            return false; // not yet time to retry
        });
    }
}