package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.usersRef = db.collection("users");
    }

    public Task<Void> addUserToDatabase(User user) {
        return usersRef
            .document(user.getUserID())
            .set(user);
    }

    public Task<DocumentSnapshot> getUserByUserId(String userID) {
        return usersRef
            .document(userID)
            .get();
    }

    //TODO: Implement UPDATE Methods

    //TODO: Implement DELETE Methods
}
