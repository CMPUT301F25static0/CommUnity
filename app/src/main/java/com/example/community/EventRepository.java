package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Updates event poster URL and imageID
     * @param eventID The event ID
     * @param posterURL The poster download URL
     * @param posterImageID The Image document ID in Firestore
     * @return Task that completes when update is done
     */
    public Task<Void> updateEventPoster(String eventID, String posterURL, String posterImageID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("posterImageURL", posterURL);
        updates.put("posterImageID", posterImageID);

        return eventsRef
                .document(eventID)
                .update(updates);
    }

    /**
     * Clears event poster URL and imageID
     * @param eventID The event ID
     * @return Task that completes when update is done
     */
    public Task<Void> clearEventPoster(String eventID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("posterImageURL", null);
        updates.put("posterImageID", null);

        return eventsRef
                .document(eventID)
                .update(updates);
    }

    /**
     * Updates event QR code URL and imageID
     * @param eventID The event ID
     * @param qrCodeURL The QR code download URL
     * @param qrCodeImageID The Image document ID in Firestore
     * @return Task that completes when update is done
     */
    public Task<Void> updateEventQRCode(String eventID, String qrCodeURL, String qrCodeImageID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("qrCodeImageURL", qrCodeURL);
        updates.put("qrCodeImageID", qrCodeImageID);

        return eventsRef
                .document(eventID)
                .update(updates);
    }

    /**
     * Clears event QR code URL and imageID
     * @param eventID The event ID
     * @return Task that completes when update is done
     */
    public Task<Void> clearEventQRCode(String eventID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("qrCodeImageURL", null);
        updates.put("qrCodeImageID", null);

        return eventsRef
                .document(eventID)
                .update(updates);
    }

}
