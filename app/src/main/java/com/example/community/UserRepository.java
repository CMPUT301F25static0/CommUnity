package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final String TAG = "UserRepository";

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.usersRef = db.collection("users");
    }

    public Task<Void> create(User user) {
        return usersRef.document(user.getUserID()).set(user);
    }

    public Task<User> getByID(String userID) {
        return usersRef.document(userID).get().continueWith(task -> {
            DocumentSnapshot snapshot = task.getResult();
            return snapshot.exists() ? snapshot.toObject(User.class) : null;
        });
    }

    public Task<Void> update(User user) {
        return usersRef.document(user.getUserID()).set(user);
    }

    public Task<Void> delete(String userID) {
        return usersRef.document(userID).delete();
    }

    public Task<List<User>> getAll() {
        return usersRef.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            List<User> users = new ArrayList<>();
            for (DocumentSnapshot doc : task.getResult()) {
                User user = doc.toObject(User.class);
                if (user != null) {
                    users.add(user);
                }
            }
            return users;
        });
    }
}
