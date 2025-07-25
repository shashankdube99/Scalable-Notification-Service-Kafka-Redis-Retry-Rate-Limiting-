package com.shashank.kafkarabbit.model;

public class RetryNotification {
    private NotificationRequest request;
    private int attempts;
    private long nextRetryTimeMillis;

    public RetryNotification(NotificationRequest request, int attempts, long delayInMillis) {
        this.request = request;
        this.attempts = attempts;
        this.nextRetryTimeMillis = System.currentTimeMillis() + delayInMillis;
    }

    public NotificationRequest getRequest() {
        return request;
    }

    public int getAttempts() {
        return attempts;
    }

    public long getNextRetryTimeMillis() {
        return nextRetryTimeMillis;
    }

    public void incrementAttempts(long delayInMillis) {
        this.attempts++;
        this.nextRetryTimeMillis = System.currentTimeMillis() + delayInMillis;
    }
}