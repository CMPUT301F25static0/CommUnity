package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class EventRepository {

    private final String TAG = "EventRepository";

    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    public EventRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.eventsRef = db.collection("events");
    }

    public Task<Void> create(Event event) {
        return eventsRef.document(event.getEventID()).set(event);
    }

    public Task<Event> getByID(String eventID) {
        return eventsRef.document(eventID).get().continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            return snapshot.exists() ? snapshot.toObject(Event.class) : null;
        });
    }

    public Task<Void> update(Event event) {
        return eventsRef.document(event.getEventID()).set(event);
    }

    // US 03.01.01
    public Task<Void> delete(String eventID) {
        return eventsRef.document(eventID).delete();
    }

    //
    public Task<List<Event>> getAll() {
        return eventsRef.get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    List<Event> events = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        Event event = doc.toObject(Event.class);
                        if (event != null) {
                            events.add(event);
                        }
                    }
                    return events;
                });
    }

    public Task<List<Event>> listEventsByOrganizer(String organizerID, int limit,
                                                   String startAfterID) {
        com.google.firebase.firestore.Query query =
                eventsRef.whereEqualTo("organizerID", organizerID).limit(limit);

        if (startAfterID != null) {
            query = query.startAfter(startAfterID);
        }

        return query.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            List<Event> events = new ArrayList<>();
            for (DocumentSnapshot doc : task.getResult()) {
                Event event = doc.toObject(Event.class);
                if (event != null) {
                    events.add(event);
                }
            }
            return events;
        });
    }

    public Task<List<Event>> listUpcoming(String fromDate, String toDate, List<String> tags,
                                          int limit, String startAfterID) {

        Query query = eventsRef
                .whereEqualTo("status", EventStatus.OPEN.name());  // only open events

        if (fromDate != null) {
            query = query.whereGreaterThanOrEqualTo("eventStartDate", fromDate);
        }
        if (toDate != null) {
            query = query.whereLessThanOrEqualTo("eventStartDate", toDate);
        }
        if (tags != null && !tags.isEmpty()) {
            query = query.whereArrayContains("tags", tags.get(0));
        }

        query = query.limit(limit);
        if (startAfterID != null) {
            query = query.startAfter(startAfterID);
        }

        return query.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            java.util.List<Event> events = new java.util.ArrayList<>();
            for (com.google.firebase.firestore.DocumentSnapshot doc : task.getResult()) {
                Event e = doc.toObject(Event.class);
                if (e != null) {
                    events.add(e);
                    Log.d("EventRepository", "Found event: " +e.getTitle());
                }
            }
            Log.d("EventRepository", "Found " + events.size() + " events");
            return events;
        });
    }



}
