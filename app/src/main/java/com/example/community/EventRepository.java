package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventRepository {
    public static final String TAG = "EventRepository";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private static final String SUBCOLLECTION_WAITLIST = "waitlist";

    public EventRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.eventsRef = db.collection("events");
    }

    public Task<Void> createEvent(Event event) {
        return eventsRef
            .document(event.getEventID())
            .set(event)
            .addOnSuccessListener(aVoid -> Log.d(TAG, "Event created successfully"))
            .addOnFailureListener(e -> Log.e(TAG, "Error creating event", e));
    }

    public Task<Event> getEventByEventID(String eventID) {
        return eventsRef
            .document(eventID)
            .get()
            .continueWith(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    Log.d(TAG + "(getEventByEventID)", "Event found with ID: " + eventID);
                    return task.getResult().toObject(Event.class);
                }
                Log.e(TAG + "(getEventByEventID)", "Event not found with ID: " + eventID);
                throw new Exception("Event not found");
        });
    }

    public Task<List<Event>> getAllEvents() {
        return eventsRef
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "All events retrieved");
                        return task.getResult().toObjects(Event.class);
                    }
                    Log.e(TAG+"(getAllEvents)", "Error getting all events", task.getException());
                    throw task.getException();
                });
    }

    public Task<Void> updateEvent(Event event) {
        return eventsRef
                .document(event.getEventID())
                .set(event)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Event updated successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating event", e));
    }

    public Task<Void> deleteEvent(String eventId) {
        return eventsRef.
                document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Event deleted successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error deleting event", e));
    }

    public Task<List<String>> getWaitlistOfEventByEventId (String eventId) {
        return eventsRef
                .document(eventId)
                .collection(SUBCOLLECTION_WAITLIST)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getWaitlistOfEventByEventId)", "Waitlist found with event ID: " + eventId);
                        return task.getResult().toObjects(String.class);
                    }
                    Log.e(TAG + "(getWaitlistOfEventByEventId)", "Waitlist not found with event ID: " + eventId);
                    throw new Exception("Waitlist not found");
                });
    }

    public void updateEventPoster() {
        // Implement
        return;
    }

    public void updateQrCode() {
        // Implement
        return;
    }




}
