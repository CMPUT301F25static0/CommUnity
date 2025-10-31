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
    
}
