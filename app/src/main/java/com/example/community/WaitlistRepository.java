package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class WaitlistRepository {

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
            .set(entry);
    }

    public Task<QuerySnapshot> getEntryByUserAndEvent(String userID, String eventID) {
        return eventsRef
            .document(eventID)
            .collection(SUBCOLLECTION_WAITLIST)
            .whereEqualTo("userID", userID)
            .get();
    }

    //TODO: Implement UPDATE Methods

    //TODO: Implement DELETE Methods
}
