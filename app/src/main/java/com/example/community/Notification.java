package com.example.community;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    public enum NotificationType {
        WON,
        LOST,
        CANCELLED,
        WAITLIST,
        INFO}

    private UUID notificationID;
    private UUID recipientID;
    private UUID eventID;
    private LocalDateTime issueDate;
    private String message;

    public Notification(UUID notificationID, UUID recipientID, UUID eventID,
                        LocalDateTime issueDate, String message) {
        this.notificationID = notificationID;
        this.recipientID = recipientID;
        this.eventID = eventID;
        this.issueDate = issueDate;
        this.message = message;
    }

    public UUID getNotificationID() {
        return notificationID;
    }

    public UUID getRecipientID() {
        return recipientID;
    }

    public UUID getEventID() {
        return eventID;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public String getMessage() {
        return message;
    }
}
