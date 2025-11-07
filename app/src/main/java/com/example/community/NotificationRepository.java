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

public class NotificationRepository {

    private final String TAG = "NotificationRepository";

    private final FirebaseFirestore db;
    private final CollectionReference notificationsRef;

    public NotificationRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.notificationsRef = db.collection("notifications");
    }

    public Task<Void> create(Notification notification) {
        return notificationsRef.document(notification.getNotificationID()).set(notification);
    }

    public Task<Void> createMany(String eventID, List<String> recipientIDs, NotificationType type, String message) {
        List<Task<Void>> writes = new ArrayList<>();

        for (String recipientID : recipientIDs) {
            Notification n = new Notification();
            n.setNotificationID(java.util.UUID.randomUUID().toString());
            n.setRecipientID(recipientID);
            n.setEventID(eventID);
            n.setType(type);
            n.setMessage(message);
            n.setIssueDate(System.currentTimeMillis());

            writes.add(create(n));
        }

        return com.google.android.gms.tasks.Tasks.whenAll(writes);
    }

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
