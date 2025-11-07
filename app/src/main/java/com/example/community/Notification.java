package com.example.community;

import com.google.firebase.firestore.DocumentId;

import java.time.LocalDateTime;

public class Notification {

    @DocumentId
    private String notificationID;
    private String recipientID;
    private String eventID;
    private long issueDate;
    private String message;
    private NotificationType type;

    public Notification() {
    }

    public Notification(String notificationID, String recipientID, String eventID,
                        long issueDate, String message, NotificationType type) {
        this.notificationID = notificationID;
        this.recipientID = recipientID;
        this.eventID = eventID;
        this.issueDate = issueDate;
        this.message = message;
        this.type = type;
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

    public long getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(long issueDate) {
        this.issueDate = issueDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
