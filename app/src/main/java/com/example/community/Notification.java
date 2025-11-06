package com.example.community;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Notification {
    @DocumentId
    private String notificationId;


    private String recipientId;
    private String eventId;
    private String eventName;

    private NotificationType type;
    private String title;
    private String message;
    private Timestamp sentAt;



    public Notification() { }

    public Notification(String eventId, String recipientId, String eventName,
                        NotificationType type) {
        this.notificationId = java.util.UUID.randomUUID().toString();
        this.eventId = eventId;
        this.recipientId = recipientId;
        this.eventName = eventName;

        this.type = type;
//        this.title = title;
//        this.message = message;
        this.sentAt = Timestamp.now();
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(String invitationId) {
        this.notificationId = invitationId;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
}
