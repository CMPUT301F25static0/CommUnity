package com.example.community;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

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

    public User() { }

    public User(String deviceToken, String userID, String username, String email) {
        this.deviceToken = deviceToken;
        this.userID = userID;
        this.username = username;
        this.email = email;
    }


    public String getDeviceToken() {
        return deviceToken;
    }
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public List<String> getInterests() {
        return interests;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getWaitingListsJoinedIDs() {
        return waitingListsJoinedIDs;
    }
    public void setWaitingListsJoinedIDs(List<String> waitingListsJoinedIDs) {
        this.waitingListsJoinedIDs = waitingListsJoinedIDs;
    }

    public List<String> getAttendingListsIDs() {
        return attendingListsIDs;
    }
    public void setAttendingListsIDs(List<String> attendingListsIDs) {
        this.attendingListsIDs = attendingListsIDs;
    }

    public List<String> getRegistrationHistoryIDs() {
        return registrationHistoryIDs;
    }
    public void setRegistrationHistoryIDs(List<String> registrationHistoryIDs) {
        this.registrationHistoryIDs = registrationHistoryIDs;
    }

    public Boolean getReceiveNotifications() {
        return receiveNotifications;
    }
    public void setReceiveNotifications(Boolean receiveNotifications) {
        this.receiveNotifications = receiveNotifications;
    }


    public void enableNotifications() {
        this.receiveNotifications = true;
    }

    public void disableNotifications() {
        this.receiveNotifications = false;
    }

    public void addInterest(String interest) {
        if (interest != null && !interests.contains(interest)) {
            interests.add(interest);
        }
    }

    public void removeInterest(String interest) {
        interests.remove(interest);
    }

    public boolean hasInterest(String interest) {
        return interests.contains(interest);
    }

    public void clearInterests() {
        interests.clear();
    }

    public void addEventToWaitlist(String eventId) {
        if (eventId != null && !waitingListsJoinedIDs.contains(eventId)) {
            waitingListsJoinedIDs.add(eventId);
            addToRegistrationHistory(eventId);
        }
    }

    public void removeEventFromWaitingList(String eventId) {
        waitingListsJoinedIDs.remove(eventId);
    }

    public boolean hasEventInWaitlist(String eventId) {
        return waitingListsJoinedIDs.contains(eventId);
    }

    public void addEventToAttendingList(String eventId) {
        if (eventId != null && !attendingListsIDs.contains(eventId)) {
            attendingListsIDs.add(eventId);
            // Remove from waitlist when they accept/attend
            removeEventFromWaitingList(eventId);
        }
    }

    public void removeEventFromAttendingList(String eventId) {
        attendingListsIDs.remove(eventId);
    }

    public boolean hasEventInAttendingList(String eventId) {
        return attendingListsIDs.contains(eventId);
    }

    public void addToRegistrationHistory(String eventId) {
        if (eventId != null && !registrationHistoryIDs.contains(eventId)) {
            registrationHistoryIDs.add(eventId);
        }
    }

    public boolean hasEventInRegistrationHistory(String eventId) {
        return registrationHistoryIDs.contains(eventId);
    }

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }
}
