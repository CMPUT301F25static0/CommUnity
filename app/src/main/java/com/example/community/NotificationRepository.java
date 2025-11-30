package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository for managing notification data in Firestore.
 * Handles all database operations for notifications.
 */
public class NotificationRepository {

    private final String TAG = "NotificationRepository";

    private final FirebaseFirestore db;
    private final CollectionReference notificationsRef;

    /**
     * Creates a new NotificationRepository instance.
     * Initializes Firestore connection.
     */
    public NotificationRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.notificationsRef = db.collection("notifications");
    }

    /**
     * Saves a notification to the database.
     *
     * @param notification notification to create
     * @return task that completes when creation finishes
     */
    public Task<Void> create(Notification notification) {
        return notificationsRef.document(notification.getNotificationID()).set(notification);
    }

    /**
     * Creates multiple notifications for a list of recipients.
     *
     * @param eventID ID of the related event
     * @param recipientIDs list of user IDs to notify
     * @param type type of notification
     * @param message notification message
     * @return task that completes when all notifications are created
     */
    public Task<Void> createMany(String eventID, List<String> recipientIDs, NotificationType type, String title, String message) {
        List<Task<Void>> writes = new ArrayList<>();

        for (String recipientID : recipientIDs) {
            Notification n = new Notification();
            n.setNotificationID(java.util.UUID.randomUUID().toString());
            n.setRecipientID(recipientID);
            n.setEventID(eventID);
            n.setType(type);
            n.setTitle(title);
            n.setMessage(message);
            n.setIssueDate(System.currentTimeMillis());

            writes.add(create(n));
        }

        return com.google.android.gms.tasks.Tasks.whenAll(writes);
    }

    /**
     * Lists notifications for a specific user with pagination.
     *
     * @param recipientID ID of the user
     * @param limit maximum number to return
     * @param startAfterID ID to start pagination after
     * @return task containing list of notifications
     */
    public Task<List<Notification>> listNotificationsByRecipient(String recipientID, int limit, String startAfterID) {
        Query query = notificationsRef.whereEqualTo("recipientID", recipientID).orderBy("issueDate").limit(limit);

        if (startAfterID != null) {
            query = query.startAfter(startAfterID);
        }

        return query.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            List<Notification> notifications = new ArrayList<>();
            for (DocumentSnapshot doc : task.getResult()) {
                Notification n = doc.toObject(Notification.class);
                if (n != null) {
                    notifications.add(n);
                }
            }
            return notifications;
        });
    }

    /**
     * Lists notifications for a specific event with pagination.
     *
     * @param eventID ID of the event
     * @param limit maximum number to return
     * @param startAfterID ID to start pagination after
     * @return task containing list of notifications
     */
    public Task<List<Notification>> listNotificationsByEvent(String eventID, int limit, String startAfterID) {
        Query query = notificationsRef.whereEqualTo("eventID", eventID).orderBy("issueDate").limit(limit);

        if (startAfterID != null) {
            query = query.startAfter(startAfterID);
        }

        return query.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            List<Notification> notifications = new ArrayList<>();
            for (DocumentSnapshot doc : task.getResult()) {
                Notification n = doc.toObject(Notification.class);
                if (n != null) {
                    notifications.add(n);
                }
            }
            return notifications;
        });
    }
}
