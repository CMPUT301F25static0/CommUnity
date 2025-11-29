package com.example.community;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service layer for user operations.
 * Handles authentication, user management, and profile updates.
 */
public class UserService {
    private static final String TAG = "UserService";
    private UserRepository userRepository;
    private FirebaseAuth firebaseAuth;

    /**
     * Creates a new UserService instance.
     * Initializes required repositories and Firebase Auth.
     */
    public UserService() {
        userRepository = new UserRepository();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Authenticates the current device using Firebase anonymous auth.
     *
     * @return task containing the authenticated Firebase user
     */
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

    /**
     * Authenticates device and creates user if needed for splash screen.
     *
     * @return task containing the user object
     */
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

    /**
     * Creates a new user from a Firebase authenticated user.
     *
     * @param firebaseUser authenticated Firebase user
     * @return task containing the created user
     */

    public Task<User> createUser(FirebaseUser firebaseUser) {
        String deviceUid = firebaseUser.getUid();

        User u = new User();
        u.setUserID(UUID.randomUUID().toString());
        u.setDeviceToken(deviceUid);
        // role defaults to ENTRANT, optional fields remain null

        return userRepository.create(u)
                .continueWithTask(v -> Tasks.forResult(u));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userID ID of the user
     * @return task containing the user
     */
    public Task<User> getByUserID(String userID) {
        return userRepository.getByUserID(userID);
    }

    /**
     * Retrieves a user by their device token.
     *
     * @param deviceToken device token
     * @return task containing the user
     */
    public Task<User> getByDeviceToken(String deviceToken) {
        return userRepository.getByDeviceToken(deviceToken);
    }

    /**
     * Gets the current device's authentication token.
     *
     * @return device token string
     */
    public String getDeviceToken() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    /**
     * Gets a user ID from their device token.
     *
     * @param deviceToken device token
     * @return task containing the user ID
     */
    public Task<String> getUserIDByDeviceToken (String deviceToken) {
        return userRepository.getByDeviceToken(deviceToken)
                .continueWithTask(lookupTask -> {
                    User existing = lookupTask.getResult();
                    if (existing != null) {
                        return Tasks.forResult(existing.getUserID());
                    }
                    return Tasks.forException(new IllegalStateException("User not found: " + deviceToken));
                });
    }

    /**
     * Retrieves all users in the system.
     *
     * @return task containing list of all users
     */
    public Task<java.util.List<User>> getAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Updates an existing user's information.
     *
     * @param user user with updated data
     * @return task that completes when update finishes
     */
    public Task<Void> updateUser(User user) {
        return userRepository.update(user);
    }

    /**
     * Deletes a user from the system.
     *
     * @param userID ID of the user to delete
     * @return task that completes when deletion finishes
     */
    public Task<Void> deleteUser(String userID) {
        return userRepository.delete(userID);
    }

    /**
     * Changes a user's role in the system.
     *
     * @param userID ID of the user
     * @param role new role to assign
     * @return task that completes when role is updated
     */
    public Task<Void> setRole(String userID, Role role) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    user.setRole(role);
                    return userRepository.update(user);
                });
    }

    /**
     * Updates a user's username.
     *
     * @param userID ID of the user
     * @param username new username
     * @return task that completes when username is updated
     */
    public Task<Void> setUsername(String userID, String username) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    user.setUsername(username);
                    return userRepository.update(user);
                });
    }

    /**
     * Updates a user's email address.
     *
     * @param userID ID of the user
     * @param email new email address
     * @return task that completes when email is updated
     */
    public Task<Void> setEmail(String userID, String email) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    user.setEmail(email);
                    return userRepository.update(user);
                });
    }

    /**
     * Updates a user's phone number.
     *
     * @param userID ID of the user
     * @param phone new phone number
     * @return task that completes when phone is updated
     */
    public Task<Void> setPhoneNumber(String userID, String phone) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    user.setPhoneNumber(phone);
                    return userRepository.update(user);
                });
    }

    /**
     * Turns on notifications for a user.
     *
     * @param userID ID of the user
     * @return task that completes when setting is updated
     */
    public Task<Void> enableNotifications(String userID) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    user.enableNotifications();
                    return userRepository.update(user);
                });
    }

    /**
     * Turns off notifications for a user.
     *
     * @param userID ID of the user
     * @return task that completes when setting is updated
     */
    public Task<Void> disableNotifications(String userID) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User user = t.getResult();
                    if (user == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    user.disableNotifications();
                    return userRepository.update(user);
                });
    }

    /**
     * Adds an event to a user's list of created events.
     *
     * @param userID ID of the user
     * @param eventID ID of the event
     * @return task that completes when list is updated
     */
    public Task<Void> addEventCreated(String userID, String eventID) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User u = t.getResult();
                    if (u == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    if (u.hasEventCreated(eventID)) {
                        return Tasks
                                .forException(new IllegalArgumentException("Event already recorded as created"));
                    }
                    u.addEventCreated(eventID);
                    return userRepository.update(u);
                });
    }

    /**
     * Removes an event from a user's list of created events.
     *
     * @param userID ID of the user
     * @param eventID ID of the event
     * @return task that completes when list is updated
     */
    public Task<Void> removeEventCreated(String userID, String eventID) {
        return userRepository.getByUserID(userID)
                .continueWithTask(t -> {
                    User u = t.getResult();
                    if (u == null) return Tasks
                            .forException(new IllegalStateException("User not found: " + userID));
                    if (!u.hasEventCreated(eventID)) {
                        return Tasks
                                .forException(new IllegalArgumentException("Event not in eventsCreatedIDs"));
                    }
                    u.removeEventCreated(eventID);
                    return userRepository.update(u);
                });
    }

    /**
     * Gets all events created by a user.
     *
     * @param userID ID of the user
     * @return task containing list of event IDs
     */
    public Task<List<String>> listEventsCreated(String userID) {
        return userRepository.getByUserID(userID)
                .continueWith(t -> {
                    User u = t.getResult();
                    if (u == null) return new ArrayList<String>();
                    // return live list per your convention; callers should avoid mutating it directly
                    return u.getEventsCreatedIDs();
                });
    }
    
    // Add to existing UserService class
    /**
     * Gets registration history for a user.
     *
     * @param userID ID of the user
     * @return task containing list of event IDs in registration history
     */
    public Task<List<String>> getRegistrationHistory(String userID) {
        return userRepository.getByUserID(userID).continueWith(task -> {
            User user = task.getResult();
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }
            return new ArrayList<>(user.getRegistrationHistoryIDs());
        });
    }

}
