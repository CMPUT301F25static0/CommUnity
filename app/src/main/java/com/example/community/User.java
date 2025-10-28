package com.example.community;

import android.media.Ringtone;

import java.util.List;
import java.util.UUID;

public class User {
    public enum Role {
        ENTRANT,
        ORGANIZER,
        ADMIN
    }

    private String deviceToken;
    private String userID;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role = Role.ENTRANT;

    List<String> interests;
    List<Event> waitingListsJoined;
    List<Event> attendingLists;
    List<Event> registrationHistory;
    Boolean receiveNotifications;

    public User(String deviceToken, String userID, String username, String email) {
        this.deviceToken = deviceToken;
        this.userID = userID;
        this.username = username;
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public List<String> getInterests() {
        return interests;
    }

    public List<Event> getWaitingListsJoined() {
        return waitingListsJoined;
    }

    public List<Event> getAttendingLists() {
        return attendingLists;
    }

    public List<Event> getRegistrationHistory() {
        return registrationHistory;
    }

    public Boolean getReceiveNotifications() {
        return receiveNotifications;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
