package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class WaitlistRepository {
    public static final String TAG = "WaitlistRepository";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private static final String SUBCOLLECTION_WAITLIST = "waitlist";

    public WaitlistRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.eventsRef = db.collection("events");
    }

    public Task<Void> addEntryToEventWaitlist(WaitingListEntry entry) {
        return eventsRef
            .document(entry.getEventID())
            .collection(SUBCOLLECTION_WAITLIST)
            .document(entry.getUserID())
            .set(entry)
            .addOnSuccessListener(aVoid -> Log.d(TAG + "(addEntryToEventWaitlist)", "Entry added successfully"))
            .addOnFailureListener(e -> Log.e(TAG + "(addEntryToEventWaitlist)", "Error adding entry", e));
    }

    public Task<WaitingListEntry> getEntryByUserAndEvent(String userId, String eventId) {
        return eventsRef
            .document(eventId)
            .collection(SUBCOLLECTION_WAITLIST)
            .document(userId)
            .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getEntryByUserAndEvent)", "Entry found with user ID: " + userId + " and event ID: " + eventId);
                        return task.getResult().toObject(WaitingListEntry.class);
                    }
                    Log.e(TAG + "(getEntryByUserAndEvent)", "Entry not found with user ID: " + userId + " and event ID: " + eventId);
                    throw new Exception("Entry not found");
                });
    }

    public Task<List<WaitingListEntry>> getWaitlistForEvent (String eventId) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Witlist retrieved for " + eventId);
                        return task.getResult().toObjects(WaitingListEntry.class);
                    }
                    Log.e(TAG+"(getWaitlistForEvent)", "Error getting waitlist", task.getException());
                    throw task.getException();
                });
    }

    public Task<List<WaitingListEntry>> getEntriesByStatus(String eventId, EntryStatus status) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .whereEqualTo("status", status)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Entries with status: " + status + " found");
                        return task.getResult().toObjects(WaitingListEntry.class);
                    }
                    Log.e(TAG + "(getEntriesByStatus)", "Entries with status: " + status + " not found", task.getException());
                    throw new Exception("Entries not found");
                });
    }

    public Task<Void> updateEntryStatus(String eventId, String userId, EntryStatus newStatus) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .document(userId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Entry status updated successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating entry status", e));
    }

    public void sampleWaitlist() {
        //Implement
        return;
    }

    public Task<Void> removeFromWaitlist(String eventId, String userId) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Entry removed from waitlist successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error removing entry from waitlist", e));
    }

    public Task<Integer> countWaitlistEntries(String eventId) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        Log.d(TAG, "Waitlist count for event " + eventId + ": " + count);
                        return count;
                    }
                    Log.e(TAG + "(countWaitlistEntries)", "Error counting waitlist entries", task.getException());
                    throw task.getException();
                });
    }

    public Task<Integer> getInvitedCount(String eventId) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .whereEqualTo("status", EntryStatus.INVITED)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        Log.d(TAG, "Invited count: " + count);
                        return count;
                    }
                    Log.w(TAG, "Error getting invited count", task.getException());
                    throw task.getException();
                });
    }

    public Task<Integer> getAcceptedCount(String eventId) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .whereEqualTo("status", EntryStatus.ACCEPTED)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        Log.d(TAG, "Accepted count: " + count);
                        return count;
                    }
                    Log.w(TAG, "Error getting accepted count", task.getException());
                    return 0;
                });
    }



    //TODO: Implement UPDATE Methods

    //TODO: Implement DELETE Methods
}
