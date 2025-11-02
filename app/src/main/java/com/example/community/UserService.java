package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserService {
    private static final String TAG = "UserService";
    private UserRepository userRepository;
    private FirebaseAuth firebaseAuth;
    UserService() {
        userRepository = new UserRepository();
        firebaseAuth = FirebaseAuth.getInstance();
    }

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
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG + "(authenticateByDevice)", "User authenticated with UID: " + user.getUid());
                return user;
            });

    }

    public Task<User> createUser(FirebaseUser firebaseUser) {
        String deviceId = firebaseUser.getUid();

        User newUser = new User(deviceId);

        return userRepository.addNewUserToDatabaseIfNotExists(newUser)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG+"(createUser)", "User created successfully with deviceID: " + deviceId);
                        return newUser;
                    } else {
                        Log.e(TAG+"(createUser)", "Failed to create user", task.getException());
                        throw task.getException();
                    }
                });

    }

    public Task<User> splashScreenDeviceAuthentication() {
        return authenticateByDevice()
                .onSuccessTask(firebaseUser -> {
                    String uid = firebaseUser.getUid();

                    User user = new User(uid);
                    return userRepository.addNewUserToDatabaseIfNotExists(user)
                            .onSuccessTask(v -> userRepository.getUserByUserId(uid));
                });
    }

    public void deleteUser() {

    }

    public void updateUser() {

    }

//    public User getUserBYID(String userID) {
//
//    }
//
//    public List<User> getAllUsers() {
//
//    }
}
