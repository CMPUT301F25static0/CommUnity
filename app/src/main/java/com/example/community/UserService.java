package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserService {
    private static final String TAG = "UserService";
    private UserRepository userRepository;
    private FirebaseAuth firebaseAuth;

    public UserService() {
        userRepository = new UserRepository();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // US 01.07.01
    public Task<FirebaseUser> authenticateByDevice() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "User already authenticated with UID: " + currentUser.getUid());
            return Tasks.forResult(currentUser);
        }

        Log.d(TAG, "Starting authentication process");
        return firebaseAuth.signInAnonymously()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG + "(authenticateByDevice)", "Anonymous auth failed", task.getException());
                        throw task.getException();
                    }
                    FirebaseUser user = task.getResult().getUser();
                    Log.d(TAG + "(authenticateByDevice)", "User authenticated with UID: " + user.getUid());
                    return user;
                });

    }

    // US 01.07.01
    public Task<User> splashScreenDeviceAuthentication() {
        return authenticateByDevice()
                .onSuccessTask(firebaseUser -> {
                    String uid = firebaseUser.getUid();

                    return createUser(firebaseUser)
                            .onSuccessTask(v -> userRepository.getByID(uid));
                });
    }

    // Helper plus  US 01.02.01, US 01.02.02
    public Task<User> createUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        User newUser = new User(uid);

        return userRepository.getByID(uid)
                .onSuccessTask(existing -> {
                    if (existing != null) {
                        Log.d(TAG + "(createUser)", "User already exists with uid: " + uid);
                        return Tasks.forResult(existing);
                    }

                    return userRepository.create(newUser)
                            .continueWith(task -> {
                                if (!task.isSuccessful()) {
                                    Log.e(TAG + "(createUser)", "Failed to create user", task.getException());
                                    throw task.getException();
                                }
                                Log.d(TAG + "(createUser)", "User created successfully with uid: " + uid);
                                return newUser;
                            });
                });
    }



    // Helper
    public Task<User> getUserByID(String userID) {
        return userRepository.getByID(userID);
    }

    // US 03.05.01, US 03.02.01
    public Task<List<User>> getAllUsers() {
        return userRepository.getAll();
    }

    // US 01.02.02
    public Task<Void> updateUser(User user) {
        return userRepository.update(user);
    }

    // US 01.02.04. US 03.02.01
    public Task<Void> deleteUser(String userID) {
        return userRepository.delete(userID);
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public Task<Void> updateUserRole(Role role) {
        String current_user = getCurrentUser().toString();
        return userRepository.getByID(current_user)
                .onSuccessTask(user -> {
                    if (user == null) {
                        Log.e(TAG + "(updateUserRole)", "User not found with uid: " + userID);
                        return Tasks.forException(new IllegalArgumentException("User not found"));
                    }
                    user.setRole(role);
                    return userRepository.update(user);
                });
    }
}
