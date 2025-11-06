package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class InvitationNotificationRepository {
    private static final String TAG = "InvitationNotificationRepository";

    private FirebaseFirestore db;
    private CollectionReference invitationNotifRef;

    public InvitationNotificationRepository() {
        db = FirebaseFirestore.getInstance();
        invitationNotifRef = db.collection("invitationNotifications");
    }

    public Task<Void> createInvitation(InvitationNotification invitationNotification) {
        return invitationNotifRef.document(invitationNotification.getInvitationId())
                .set(invitationNotification)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Invitation created successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error creating invitation", e));
    }

    public Task<Void> updateInvitationStatus(String invitationId, EntryStatus status) {
        return invitationNotifRef.document(invitationId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Invitation status updated successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating invitation status", e));
    }

    public Task<InvitationNotification> getInvitationById(String invitationId) {
        return invitationNotifRef.document(invitationId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        Log.d(TAG + "(getInvitationByID)", "Invitation with ID: " + invitationId + " found");
                        return task.getResult().toObject(InvitationNotification.class);
                    }
                    Log.d(TAG + "(getInvitationByID)", "Invitation with ID: " + invitationId + " not found", task.getException());
                    throw new Exception("Invitation not found");
                });
    }

    public Task<List<InvitationNotification>> getInvitationsByEventID(String eventId) {
        return invitationNotifRef.whereEqualTo("eventId", eventId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getInvitationsByEventID)", "Invitations with event ID: " + eventId + " found");
                        return task.getResult().toObjects(InvitationNotification.class);
                    }
                    Log.d(TAG + "(getInvitationsByEventID)", "Invitations with event ID: " + eventId + " not found", task.getException());
                    throw new Exception("Invitations not found");
                });
    }

    public Task<List<InvitationNotification>> getInvitationsByStatus(String eventId, EntryStatus status) {
        return invitationNotifRef.whereEqualTo("eventId", eventId)
                .whereEqualTo("status", status)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getInvitationsByStatus)", "Invitations with status: " + status + " found");
                        return task.getResult().toObjects(InvitationNotification.class);
                    }
                    Log.d(TAG + "(getInvitationsByStatus)", "Invitations with status: " + status + " not found", task.getException());
                    throw new Exception("Invitations not found");
                });
    }

    public Task<List<InvitationNotification>> getNotificationsOfUser(String userId) {
        return invitationNotifRef.whereEqualTo("entrantId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getNotificationsOfUser)", "Notifications with user ID: " + userId + " found");
                        return task.getResult().toObjects(InvitationNotification.class);
                    }
                    Log.d(TAG + "(getNotificationsOfUser)", "Notifications with user ID: " + userId + " not found", task.getException());
                    throw new Exception("Notifications not found");
                });
    }
}
