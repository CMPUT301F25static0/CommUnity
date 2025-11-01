package com.example.community;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventID; // need to make a UUID to string helper
    private String title;
    private String description;
    private User organizer;
    private String location;
    private String eventType;
    private Integer capacity;

    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;

    private String qrCodeImageURL;

    private List<String> waitListUserIDs = new ArrayList<>();
    private List<String> attendeeListUserIDs = new ArrayList<>();
    private List<String> invitedListUserIDs = new ArrayList<>();
    private List<String> cancelledListUserIDs = new ArrayList<>();

    public Event() { }

    public Event(String eventID, String title, String description, User organizer, String location,
                 String eventType, Integer capacity, LocalDateTime eventStartDate,
                 LocalDateTime eventEndDate, LocalDateTime registrationStart, LocalDateTime registrationEnd,
                 String qrCodeImageURL) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
        this.organizer = organizer;
        this.location = location;
        this.eventType = eventType;
        this.capacity = capacity;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
        this.qrCodeImageURL = qrCodeImageURL;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public User getOrganizer() {
        return organizer;
    }
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getEventStartDate() {
        return eventStartDate;
    }
    public void setEventStartDate(LocalDateTime eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public LocalDateTime getEventEndDate() {
        return eventEndDate;
    }
    public void setEventEndDate(LocalDateTime eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public LocalDateTime getRegistrationStart() {
        return registrationStart;
    }
    public void setRegistrationStart(LocalDateTime registrationStart) {
        this.registrationStart = registrationStart;
    }

    public LocalDateTime getRegistrationEnd() {
        return registrationEnd;
    }
    public void setRegistrationEnd(LocalDateTime registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public String getQrCodeImageURL() {
        return qrCodeImageURL;
    }
    public void setQrCodeImageURL(String qrCodeImageURL) {
        this.qrCodeImageURL = qrCodeImageURL;
    }

    public List<String> getWaitListUserIDs() {
        return waitListUserIDs;
    }
    public void setWaitListUserIDs(List<String> waitListUserIDs) {
        this.waitListUserIDs = waitListUserIDs;
    }

    public List<String> getAttendeeListUserIDs() {
        return attendeeListUserIDs;
    }
    public void setAttendeeListUserIDs(List<String> attendeeListUserIDs) {
        this.attendeeListUserIDs = attendeeListUserIDs;
    }

    public List<String> getInvitedListUserIDs() {
        return invitedListUserIDs;
    }
    public void setInvitedListUserIDs(List<String> invitedListUserIDs) {
        this.invitedListUserIDs = invitedListUserIDs;
    }

    public List<String> getCancelledListUserIDs() {
        return cancelledListUserIDs;
    }
    public void setCancelledListUserIDs(List<String> cancelledListUserIDs) {
        this.cancelledListUserIDs = cancelledListUserIDs;
    }


    public void addUserToWaitlist(String userID) {
        if (waitListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is already on waitlist");
        }
        waitListUserIDs.add(userID);
    }

    public void removeUserFromWaitlist(String userID) {
        if (!waitListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is not on waitlist");
        }
        waitListUserIDs.remove(userID);
    }

    public boolean hasUserInWaitlist(String userID) {
        return waitListUserIDs.contains(userID);
    }

    public void addUserToInvitedList(String userID) {
        if (invitedListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is already invited");
        }
        invitedListUserIDs.add(userID);
    }

    public void removeUserFromInvitedList(String userID) {
        if (!invitedListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is not invited");
        }
        invitedListUserIDs.remove(userID);
    }

    public boolean hasUserInInvitedList(String userID) {
        return invitedListUserIDs.contains(userID);
    }

    public void addUserToAttendeeList(String userID) {
        if (attendeeListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is already on attendee list");
        }
        attendeeListUserIDs.add(userID);
    }

    public void removeUserFromAttendeeList(String userID) {
        if (!attendeeListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is not on attendee list");
        }
        attendeeListUserIDs.remove(userID);
    }

    public boolean hasUserInAttendeeList(String userID) {
        return attendeeListUserIDs.contains(userID);
    }

    public void addUserToCancelledList(String userID) {
        if (cancelledListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is already cancelled");
        }
        cancelledListUserIDs.add(userID);
    }

    public void removeUserFromCancelledList(String userID) {
        if (!cancelledListUserIDs.contains(userID)) {
            throw new IllegalArgumentException("User is not cancelled");
        }
        cancelledListUserIDs.remove(userID);
    }

    public boolean hasUserInCancelledList(String userID) {
        return cancelledListUserIDs.contains(userID);
    }
}
