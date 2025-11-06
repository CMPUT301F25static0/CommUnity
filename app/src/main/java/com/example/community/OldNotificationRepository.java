package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NotificationRepository {
    private static final String TAG = "NotificationRepository";

    private FirebaseFirestore db;
    private CollectionReference notifRef;

    public NotificationRepository() {
        db = FirebaseFirestore.getInstance();
        this.notifRef = db.collection("notifications");
    }

    public Task<Void> createNotification(Notification notification) {
        return notifRef.document(notification.getNotificationID())
                .set(notification)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Notification created successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error creating notification", e));
    }

    public Task<List<Notification>> getNotificationByUserId (String userId) {
        return notifRef.whereEqualTo("userId", userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG + "(getNotificationByUserId)", "Notifications with user ID: " + userId + " found");
                        return task.getResult().toObjects(Notification.class);
                    }
                    Log.d(TAG + "(getNotificationByUserId)", "Notifications with user ID: " + userId + " not found", task.getException());
                    throw new Exception("Notifications not found");
                });
    }
}
