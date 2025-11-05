package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.UUID;

public class NotificationService {
    private static final String TAG = "NotificationManager";

    private NotificationRepository notificationRepository;

    NotificationService() {
        notificationRepository = new NotificationRepository();
    }

    public void createNotif() {

    }

    public Task<Void> setUpNotification(String userId, String eventId, NotificationType type,
                                        String message, String relatedInvitationId) {

        Notification notification = new Notification(userId, eventId, type, message);
        notification.setRelatedInvitationId(relatedInvitationId);

        return notificationRepository.createNotification(notification)
                .addOnSuccessListener(success -> Log.d(TAG, "Notification created successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error creating notification", e));
    }

    public void sendNotification(String userId, String title, String message) {
        /// Implement Firebase Cloud Messaging
    }

    public void updateNotif() {
        // might not be needed tbh
    }

//    public Notification getNotifByID(UUID notificationID) {
//
//    }
//
//    public List<Notification> getALLNotifs() {
//
//    }

    // probably should create helpers like getAllNotifsFromEvent
}
