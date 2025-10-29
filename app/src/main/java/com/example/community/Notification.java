package com.example.community;

import java.time.LocalDateTime;

public class Notification {
    public enum NotificationType {
        WON,
        LOST,
        CANCELLED,
        WAITLIST,
        INFO}

    private String notificationID;
    private String recipientID;
    private String eventID;
    private LocalDateTime issueDate;
    private String message;

    public Notification(String notificationID, String recipientID, String eventID,
                        LocalDateTime issueDate, String message) {
        this.notificationID = notificationID;
        this.recipientID = recipientID;
        this.eventID = eventID;
        this.issueDate = issueDate;
        this.message = message;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public String getEventID() {
        return eventID;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public String getMessage() {
        return message;
    }
}
