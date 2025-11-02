package com.example.community;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an application user and their state within the CommUnity app.
 *
 * <p>The User object holds identifying information (userID, username, email),
 * notification preferences, role information, and collections tracking the user's
 * interests and event participation (waitlists, attending lists, and registration history).
 */
public class User {
    public enum Role {
        ENTRANT,
        ORGANIZER,
        ADMIN
    }

    private String deviceToken;


    @DocumentId
    private String userID;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role = Role.ENTRANT;
    Boolean receiveNotifications = true;

    List<String> interests = new ArrayList<>();
    List<String> waitingListsJoinedIDs = new ArrayList<>();
    List<String> attendingListsIDs = new ArrayList<>();
    List<String> registrationHistoryIDs = new ArrayList<>();


    /**
     * Default no-arg constructor required for Firebase
     */
    public User() { }

    /**
     * Constructor for User object
     *
     * @param deviceToken device token
     * @param userID unique user id
     * @param username display name for the user
     * @param email user's email address
     */
//    public User(String deviceToken, String userID, String username, String email) {
//        this.deviceToken = deviceToken;
//        this.userID = userID;
//        this.username = username;
//        this.email = email;
//    }


    /**
     * Constructor for User object
     *
     * @param userID unique user id
     */
    public User(String userID) {
        this.userID = userID;
    }


    public String getDeviceToken() {
        return deviceToken;
    }
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * Returns the user id for this user.
     *
     * @return the user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user id for this user.
     *
     * @param userID the user id to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Returns the username for this user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the email address for this user.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for this user.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the phone number for this user, or null if not provided.
     *
     * @return the phone number or null
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number for this user.
     *
     * @param phoneNumber the phone number to set (may be null)
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the role assigned to this user.
     *
     * @return the user's role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Changes the role assigned to this user.
     *
     * @param role the new role to assign
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns the list of interests for the user. The returned list is the live
     * internal list; callers should avoid mutating it directly to preserve
     * invariants (use addInterest/removeInterest instead).
     *
     * @return list of interest strings
     */
    public List<String> getInterests() {
        return interests;
    }

    /**
     * Replaces the internal interests list. Passing null will set an empty list.
     *
     * @param interests new list of interests
     */
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    /**
     * Returns the list of event IDs where the user is on the waitlist.
     *
     * @return list of event ids
     */
    public List<String> getWaitingListsJoinedIDs() {
        return waitingListsJoinedIDs;
    }

    /**
     * Replaces the internal waiting list IDs. Passing null will set an empty list.
     *
     * @param waitingListsJoinedIDs new list of waiting list ids
     */
    public void setWaitingListsJoinedIDs(List<String> waitingListsJoinedIDs) {
        this.waitingListsJoinedIDs = waitingListsJoinedIDs;
    }

    /**
     * Returns the list of event IDs the user is currently attending.
     *
     * @return list of attending event ids
     */
    public List<String> getAttendingListsIDs() {
        return attendingListsIDs;
    }

    /**
     * Replaces the internal attending list IDs. Passing null will set an empty list.
     *
     * @param attendingListsIDs new list of attending ids
     */
    public void setAttendingListsIDs(List<String> attendingListsIDs) {
        this.attendingListsIDs = attendingListsIDs;
    }

    /**
     * Returns the list of event IDs the user has registered for historically.
     *
     * @return registration history ids
     */
    public List<String> getRegistrationHistoryIDs() {
        return registrationHistoryIDs;
    }

    /**
     * Replaces the internal registration history IDs. Passing null will set an empty list.
     *
     * @param registrationHistoryIDs new registration history ids
     */
    public void setRegistrationHistoryIDs(List<String> registrationHistoryIDs) {
        this.registrationHistoryIDs = registrationHistoryIDs;
    }

    /**
     * Returns whether the user currently receives push notifications.
     *
     * @return true if notifications are enabled, false otherwise
     */
    public Boolean getReceiveNotifications() {
        return receiveNotifications;
    }

    /**
     * Sets whether the user should receive push notifications.
     *
     * @param receiveNotifications true to enable notifications, false to disable
     */
    public void setReceiveNotifications(Boolean receiveNotifications) {
        this.receiveNotifications = receiveNotifications;
    }

    /**
     * Enable push notifications for this user.
     */
    public void enableNotifications() {
        this.receiveNotifications = true;
    }

    /**
     * Disable push notifications for this user.
     */
    public void disableNotifications() {
        this.receiveNotifications = false;
    }

    /**
     * Adds a new interest to the user's interest list.
     *
     * @param interest the interest label to add
     * @throws IllegalArgumentException if the interest is already present
     */
    public void addInterest(String interest) {
        if (interests.contains(interest)) {
            throw new IllegalArgumentException("Interest already in list");
        }
        if (interest == null || interest.isBlank()) {
            return;
        }
        interests.add(interest);
    }

    /**
     * Removes an interest from the user's interest list.
     *
     * @param interest the interest label to remove
     * @throws IllegalArgumentException if the interest is not found
     */
    public void removeInterest(String interest) {
        if (!interests.contains(interest)) {
            throw new IllegalArgumentException("Interest not found");
        }
        interests.remove(interest);

    }

    /**
     * Returns whether the user has a specific interest.
     *
     * @param interest interest to query
     * @return true if the interest exists, false otherwise
     */
    public boolean hasInterest(String interest) {
        return interests.contains(interest);
    }

    /**
     * Adds an event id to the user's waiting list.
     * Also records the event in the registration history.
     *
     * @param eventId id of the event to add
     * @throws IllegalArgumentException if the event is already in the waitlist
     */
    public void addEventToWaitlist(String eventId) {
        if (waitingListsJoinedIDs.contains(eventId)) {
            throw new IllegalArgumentException("Event already in waitlist");
        }
        if (attendingListsIDs.contains(eventId)) {
            throw new IllegalArgumentException("You are already attending this event");
        }
        waitingListsJoinedIDs.add(eventId);
        addToRegistrationHistory(eventId);
    }

    /**
     * Removes an event id from the user's waiting list.
     *
     * @param eventId id of the event to remove
     * @throws IllegalArgumentException if the event is not in the waitlist
     */
    public void removeEventFromWaitingList(String eventId) {
        if (!waitingListsJoinedIDs.contains(eventId)) {
            throw new IllegalArgumentException("Event not in waitlist");
        }
        waitingListsJoinedIDs.remove(eventId);
    }

    /**
     * Returns whether an event id exists in the user's waiting list.
     *
     * @param eventId id of the event to check
     * @return true if event is in the waitlist, false otherwise
     */
    public boolean hasEventInWaitlist(String eventId) {
        return waitingListsJoinedIDs.contains(eventId);
    }

    /**
     * Adds an event id to the attending list and removes it from the waitlist.
     *
     * @param eventId id of the event to add
     * @throws IllegalArgumentException if the event is already in the attending list
     */
    public void addEventToAttendingList(String eventId) {
        if (attendingListsIDs.contains(eventId)) {
            throw new IllegalArgumentException("Event already in attending list");
        }
        removeEventFromWaitingList(eventId);
        attendingListsIDs.add(eventId);
    }

    /**
     * Removes an event id from the attending list.
     *
     * @param eventId id of the event to remove
     * @throws IllegalArgumentException if the event is not in the attending list
     */
    public void removeEventFromAttendingList(String eventId) {
        if (!attendingListsIDs.contains(eventId)) {
            throw new IllegalArgumentException("Event not in attending list");
        }
        attendingListsIDs.remove(eventId);
    }

    /**
     * Returns whether an event id exists in the attending list.
     *
     * @param eventId id of the event to check
     * @return true if event is in the attending list, false otherwise
     */
    public boolean hasEventInAttendingList(String eventId) {
        return attendingListsIDs.contains(eventId);
    }

    /**
     * Adds an event id to the registration history.
     *
     * @param eventId id of the event to record
     * @throws IllegalArgumentException if the event is already in the registration history
     */
    public void addToRegistrationHistory(String eventId) {
        if (registrationHistoryIDs.contains(eventId)) {
            throw new IllegalArgumentException("Event already in registration history");
        }
        registrationHistoryIDs.add(eventId);
    }

    /**
     * Returns whether an event id exists in the user's registration history.
     *
     * @param eventId id of the event to check
     * @return true if the event is present in the registration history, false otherwise
     */
    public boolean hasEventInRegistrationHistory(String eventId) {
        return registrationHistoryIDs.contains(eventId);
    }

    /**
     * Returns whether the user has a phone number on record.
     *
     * @return true if phoneNumber is non-null and not empty, false otherwise
     */
    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }
}
