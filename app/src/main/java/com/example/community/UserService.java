package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

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

    public Task<User> splashScreenDeviceAuthentication() {
        return authenticateByDevice()
                .continueWithTask(authTask -> {
                    FirebaseUser firebaseUser = authTask.getResult();
                    String deviceUid = firebaseUser.getUid();

                    return userRepository.getByDeviceToken(deviceUid)
                            .continueWithTask(lookupTask -> {
                                User existing = lookupTask.getResult();
                                if (existing != null) {
                                    return Tasks.forResult(existing);
                                }
                                return createUser(firebaseUser);
                            });
                });
    }

    public Task<User> createUser(FirebaseUser firebaseUser) {
        String deviceUid = firebaseUser.getUid();

        User u = new User();
        u.setUserID(UUID.randomUUID().toString());
        u.setDeviceToken(deviceUid);
        // role defaults to ENTRANT, optional fields remain null

        return userRepository.create(u)
                .continueWithTask(v -> Tasks.forResult(u));
    }

    public Task<User> getByUserID(String userID) {
        return userRepository.getByUserID(userID);
    }

    public Task<User> getByDeviceToken(String deviceToken) {
        return userRepository.getByDeviceToken(deviceToken);
    }

    public Task<java.util.List<User>> getAllUsers() {
        return userRepository.getAll();
    }

    public Task<Void> updateUser(User user) {
        return userRepository.update(user);
    }

    public Task<Void> deleteUser(String userID) {
        return userRepository.delete(userID);
    }

    public Task<Void> setRole(String userID, Role role) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks.forException(new IllegalStateException("User not found: " + userID));
                    user.setRole(role);
                    return userRepository.update(user);
                });
    }

    public Task<Void> setUsername(String userID, String username) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks.forException(new IllegalStateException("User not found: " + userID));
                    user.setUsername(username);
                    return userRepository.update(user);
                });
    }

    public Task<Void> setEmail(String userID, String email) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks.forException(new IllegalStateException("User not found: " + userID));
                    user.setEmail(email);
                    return userRepository.update(user);
                });
    }

    public Task<Void> setPhoneNumber(String userID, String phone) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks.forException(new IllegalStateException("User not found: " + userID));
                    user.setPhoneNumber(phone);
                    return userRepository.update(user);
                });
    }

    public Task<Void> enableNotifications(String userID) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks.forException(new IllegalStateException("User not found: " + userID));
                    user.enableNotifications();
                    return userRepository.update(user);
                });
    }

    public Task<Void> disableNotifications(String userID) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks.forException(new IllegalStateException("User not found: " + userID));
                    user.disableNotifications();
                    return userRepository.update(user);
                });
    }
}
