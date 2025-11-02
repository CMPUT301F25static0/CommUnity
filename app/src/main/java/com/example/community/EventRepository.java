package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventRepository {

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    public EventRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.eventsRef = db.collection("events");
    }

    public Task<Void> addEventToDatabase(Event event) {
        return eventsRef
            .document(event.getEventID())
            .set(event);
    }

    public Task<DocumentSnapshot> getEventByEventID(String eventID) {
        return eventsRef
            .document(eventID)
            .get();
    }
}
