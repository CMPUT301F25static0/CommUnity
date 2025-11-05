package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InvitationRepository {
    private static final String TAG = "InvitationRepository";

    private FirebaseFirestore db;
    private CollectionReference invitationRef;

    public InvitationRepository() {
        db = FirebaseFirestore.getInstance();
        invitationRef = db.collection("invitations");
    }

    public Task<Void> createInvitation(Invitation invitation) {
        return invitationRef.document(invitation.getInvitationId())
                .set(invitation)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Invitation created successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error creating invitation", e));
    }

    public Task<Void> updateInvitationStatus(String invitationId, EntryStatus status) {
        return invitationRef.document(invitationId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Invitation status updated successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating invitation status", e));
    }

    public Task<Invitation> getInvitationByID(String invitationId) {
        return invitationRef.document(invitationId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        Log.d(TAG + "(getInvitationByID)", "Invitation with ID: " + invitationId + " found");
                        return task.getResult().toObject(Invitation.class);
                    }
                    Log.d(TAG + "(getInvitationByID)", "Invitation with ID: " + invitationId + " not found", task.getException());
                    throw new Exception("Invitation not found");
                });
    }

    public Task<List<Invitation>> getInvitationsByEventID(String eventId) {
        return invitationRef.whereEqualTo("eventId", eventId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getInvitationsByEventID)", "Invitations with event ID: " + eventId + " found");
                        return task.getResult().toObjects(Invitation.class);
                    }
                    Log.d(TAG + "(getInvitationsByEventID)", "Invitations with event ID: " + eventId + " not found", task.getException());
                    throw new Exception("Invitations not found");
                });
    }

    public Task<List<Invitation>> getInvitationsByStatus(String eventId, EntryStatus status) {
        return invitationRef.whereEqualTo("eventId", eventId)
                .whereEqualTo("status", status)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getInvitationsByStatus)", "Invitations with status: " + status + " found");
                        return task.getResult().toObjects(Invitation.class);
                    }
                    Log.d(TAG + "(getInvitationsByStatus)", "Invitations with status: " + status + " not found", task.getException());
                    throw new Exception("Invitations not found");
                });
    }
}
