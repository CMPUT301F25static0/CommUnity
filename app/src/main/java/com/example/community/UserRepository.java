package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private final String TAG = "UserRepository";

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.usersRef = db.collection("users");
    }

    public Task<Void> addNewUserToDatabaseIfNotExists(User user) {
        return usersRef
                .document(user.getUserID())
                .get()
                .onSuccessTask(snapshot ->
                        snapshot.exists() ? Tasks.forResult(null) : usersRef.document(user.getUserID()).set(user)
                );
    }

    public Task<User> getUserByUserId(String userID) {
        return usersRef
            .document(userID)
            .get()
            .continueWith(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG + "(getUserByUserId)", "Failed to get user", task.getException());
                    throw task.getException();
                }
                DocumentSnapshot userDocument = task.getResult();
                if (userDocument.exists()) {
                    Log.d(TAG + "(getUserByUserId)", "User found with ID: " + userID);
                    return userDocument.toObject(User.class);
                } else {
                    Log.d(TAG + "(getUserByUserId)", "User not found with ID: " + userID);
                    return null;
                }
            });
    }

    public Task<Void> updateUserProfile(String userId, String username, String email, String phoneNumber) {
        Map<String, Object> updates = new HashMap<>();
        if (username != null) {
            updates.put("username", username);
        }
        if (email != null) {
            updates.put("email", email);
        }
        if (phoneNumber != null) {
            updates.put("phoneNumber", phoneNumber);
        }

        return usersRef.document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User profile updated"))
                .addOnFailureListener(e -> Log.e(TAG+"(updateUserProfile)", "Failed to update user profile", e));

    }

    public Task<Void> updateNotificationPreferences(String userId, boolean allowNotifications) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("allowNotifications", allowNotifications);

        return usersRef.document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Notification preferences updated"))
                .addOnFailureListener(e -> Log.e(TAG+"(updateNotificationPreferences)", "Failed to update notification preferences", e));
    }

    public Task<List<User>> getAllUsers() {
        return usersRef.get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "All users retrieved");
                        return task.getResult().toObjects(User.class);
                    }
                    Log.e(TAG+"(getAllUsers)", "Failed to get users", task.getException());
                    throw task.getException();
                });
    }

    public Task<Void> deleteUser(String userId) {
        return usersRef.document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User deleted"))
                .addOnFailureListener(e -> Log.e(TAG+"(deleteUser)", "Failed to delete user", e));
    }

    //TODO: Implement UPDATE Methods

    //TODO: Implement DELETE Methods
}
