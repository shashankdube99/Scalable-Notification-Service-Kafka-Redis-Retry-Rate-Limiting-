package com.shashank.kafkarabbit.model;

public class NotificationRequest {
    private String to;
    private String message;

    // getters and setters

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "To: " + to + ", Message: " + message;
    }
}