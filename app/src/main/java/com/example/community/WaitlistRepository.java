package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitlistRepository {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private static final String SUBCOLLECTION_WAITLIST = "waitlist";

    public WaitlistRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.eventsRef = db.collection("events");
    }

    public Task<Void> create(WaitingListEntry entry) {
        return eventsRef.document(entry.getEventID())
                .collection(SUBCOLLECTION_WAITLIST)
                .document(entry.getUserID()).set(entry);
    }

    public Task<WaitingListEntry> getByID(String eventID, String userID) {
        return eventsRef.document(eventID).collection(SUBCOLLECTION_WAITLIST)
                .document(userID)
                .get()
                .continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            return snapshot.exists() ? snapshot.toObject(WaitingListEntry.class) : null;
        });
    }


    public Task<Void> update(WaitingListEntry entry) {
        return eventsRef.document(entry.getEventID())
                .collection(SUBCOLLECTION_WAITLIST)
                .document(entry.getUserID()).set(entry);
    }

    public Task<Void> delete(String eventID, String userID) {
        return eventsRef.document(eventID)
                .collection(SUBCOLLECTION_WAITLIST)
                .document(userID).delete();
    }

    public Task<List<WaitingListEntry>> listByEvent(String eventID) {
        return eventsRef.document(eventID)
                .collection(SUBCOLLECTION_WAITLIST)
                .get()
                .continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return task.getResult().toObjects(WaitingListEntry.class);
        });
    }

    public Task<List<WaitingListEntry>> listByEventAndStatus(String eventID, EntryStatus status) {
        return eventsRef.document(eventID)
                .collection(SUBCOLLECTION_WAITLIST)
                .whereEqualTo("status", status)
                .get()
                .continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return task.getResult().toObjects(WaitingListEntry.class);
        });
    }

    public Task<Long> countByEvent(String eventID) {
        return eventsRef.document(eventID)
                .collection(SUBCOLLECTION_WAITLIST)
                .get()
                .continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return (long) task.getResult().size();
        });
    }

    public Task<Map<EntryStatus, Long>> countsByEventGrouped(String eventID) {
        return eventsRef.document(eventID)
                .collection(SUBCOLLECTION_WAITLIST)
                .get()
                .continueWith(task -> {
            QuerySnapshot snapshot = task.getResult();
            Map<EntryStatus, Long> counts = new HashMap<>();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                WaitingListEntry entry = doc.toObject(WaitingListEntry.class);
                if (entry != null) {
                    EntryStatus status = entry.getStatus();
                    counts.put(status, counts.getOrDefault(status, 0L) + 1);
                }
            }
            return counts;
        });
    }
}
