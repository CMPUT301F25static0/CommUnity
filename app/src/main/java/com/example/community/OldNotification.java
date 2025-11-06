package com.example.community;

import com.google.firebase.Timestamp;

import java.util.UUID;

public class OldNotification {

    private String notificationID;
    private String recipientID;
    private String eventID;
    private Timestamp issueDate;
    private String message;
    private NotificationType type;
    private String relatedInvitationId;



    public OldNotification() { }

    public OldNotification(String recipientID, String eventID,
                           NotificationType type, String message) {
        this.notificationID = notificationID;
        this.recipientID = recipientID;
        this.eventID = eventID;
        this.issueDate = Timestamp.now();
        this.message = message;
        this.type = type;
    }

    private String generateNotificationID() {
        return "NOTIF_" + UUID.randomUUID().toString();
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

    public Timestamp getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(Timestamp issueDate) {
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

    public String getRelatedInvitationId() {
        return relatedInvitationId;
    }
    public void setRelatedInvitationId(String relatedInvitationId) {
        this.relatedInvitationId = relatedInvitationId;
    }
}
