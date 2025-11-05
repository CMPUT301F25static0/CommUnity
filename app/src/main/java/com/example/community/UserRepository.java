package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                    Log.d(TAG + "(getUserByUserId)", "Failed to get user", task.getException());
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

    //TODO: Implement UPDATE Methods

    //TODO: Implement DELETE Methods
}
