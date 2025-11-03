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

    public Notification() { }

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
    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getRecipientID() {
        return recipientID;
    }
    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
