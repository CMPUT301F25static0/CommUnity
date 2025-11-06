package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;

public class OldNotificationService {
    private static final String TAG = "NotificationManager";

    private OldNotificationRepository oldNotificationRepository;

    OldNotificationService() {
        oldNotificationRepository = new OldNotificationRepository();
    }

    public void createNotif() {

    }

    public Task<Void> setUpNotification(String userId, String eventId, NotificationType type,
                                        String message, String relatedInvitationId) {

        OldNotification oldNotification = new OldNotification(userId, eventId, type, message);
        oldNotification.setRelatedInvitationId(relatedInvitationId);

        return oldNotificationRepository.createNotification(oldNotification)
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
