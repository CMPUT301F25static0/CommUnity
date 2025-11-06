package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

public class NotificationRepository {
    private static final String TAG = "NotificationRepository";

    private FirebaseFirestore db;
    private CollectionReference notifRef;

    public NotificationRepository() {
        db = FirebaseFirestore.getInstance();
        notifRef = db.collection("notifications");
    }

    public Task<Void> createNotification(Notification notification) {
        return notifRef.document(notification.getNotificationId())
                .set(notification)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Notification created successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error creating Notification", e));
    }

    public Task<Void> createNotificationBatch(List<Notification> notifications) {
        WriteBatch batch = db.batch();

        for (Notification notification : notifications) {
            batch.set(notifRef.document(notification.getNotificationId()), notification);
        }

        return batch.commit()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "A batch of " + notifications.size() + " notifications created successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error creating Notification batch", e));
    }


    // Might need to move to WaitlistRepository
    public Task<Void> updateNotificationStatus(String invitationId, EntryStatus status) {
        return notifRef.document(invitationId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Invitation status updated successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating invitation status", e));
    }


    public Task<Notification> getNotificationById(String notificationId) {
        return notifRef.document(notificationId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        Log.d(TAG + "(getNotificationByID)", "Invitation with ID: " + notificationId + " found");
                        return task.getResult().toObject(Notification.class);
                    }
                    Log.e(TAG + "(getNotificationByID)", "Notification with ID: " + notificationId + " not found", task.getException());
                    throw new Exception("Notification not found");
                });
    }


    // Waitlist repository
    public Task<List<Notification>> getChosenEntrants(String eventId) {
        return notifRef.whereEqualTo("eventId", eventId)
                .whereEqualTo("type", EntryStatus.INVITED)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getInvitationsByEventID)", "Invitations with event ID: " + eventId + " found");
                        return task.getResult().toObjects(Notification.class);
                    }
                    Log.d(TAG + "(getInvitationsByEventID)", "Invitations with event ID: " + eventId + " not found", task.getException());
                    throw new Exception("Invitations not found");
                });
    }


    // Waitlist repository
    public Task<List<Notification>> getInvitationsByStatus(String eventId, EntryStatus status) {
        return notifRef.whereEqualTo("eventId", eventId)
                .whereEqualTo("status", status)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getInvitationsByStatus)", "Invitations with status: " + status + " found");
                        return task.getResult().toObjects(Notification.class);
                    }
                    Log.e(TAG + "(getInvitationsByStatus)", "Invitations with status: " + status + " not found", task.getException());
                    throw new Exception("Invitations not found");
                });
    }

    public Task<List<Notification>> getNotificationsOfUser(String userId) {
        return notifRef.whereEqualTo("entrantId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getNotificationsOfUser)", "Notifications with user ID: " + userId + " found");
                        return task.getResult().toObjects(Notification.class);
                    }
                    Log.e(TAG + "(getNotificationsOfUser)", "Notifications with user ID: " + userId + " not found", task.getException());
                    throw new Exception("Notifications not found");
                });
    }
}
