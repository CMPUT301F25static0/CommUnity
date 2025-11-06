package com.example.community;

import com.google.firebase.firestore.DocumentId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    @DocumentId
    private String eventID;
    private String title;
    private String description;
    private String organizerID;
    private String location;
    private String eventType;
    private Integer maxCapacity;
    private Integer currentCapacity;
    private List<String> tags = new ArrayList<>();
    private EventStatus status = EventStatus.DRAFT;

    private String  eventStartDate;
    private String  eventEndDate;
    private String  registrationStart;
    private String  registrationEnd;

    private String qrCodeImageID;
    private String qrCodeImageURL;
    private String posterImageID;
    private String posterImageURL;

    private List<String> waitListUserIDs = new ArrayList<>();
    private List<String> attendeeListUserIDs = new ArrayList<>();
    private List<String> invitedListUserIDs = new ArrayList<>();
    private List<String> cancelledListUserIDs = new ArrayList<>();

    public Event() { }

    public Event(String eventID, String title, String description, String organizerID, String location,
                 String eventType, Integer capacity, String  eventStartDate,
                 String  eventEndDate, String  registrationStart, String  registrationEnd) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
        this.organizerID = organizerID;
        this.location = location;
        this.eventType = eventType;
        this.maxCapacity = capacity;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
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

    public String getOrganizerID() {
        return organizerID;
    }
    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
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

    public Integer getMaxCapacity() {
        return maxCapacity;
    }
    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }
    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public String  getEventStartDate() {
        return eventStartDate;
    }
    public void setEventStartDate(String  eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String  getEventEndDate() {
        return eventEndDate;
    }
    public void setEventEndDate(String  eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String  getRegistrationStart() {
        return registrationStart;
    }
    public void setRegistrationStart(String  registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String  getRegistrationEnd() {
        return registrationEnd;
    }
    public void setRegistrationEnd(String  registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public String getQRCodeImageID() {
        return qrCodeImageID;
    }
    public void setQRCodeImageID(String qrCodeImageID) {
        this.qrCodeImageID = qrCodeImageID;
    }

    public String getQRCodeImageURL() {
        return qrCodeImageURL;
    }
    public void setQRCodeImageURL(String qrCodeImageURL) {
        this.qrCodeImageURL = qrCodeImageURL;
    }

    public String getPosterImageID() {
        return posterImageID;
    }
    public void setPosterImageID(String posterImageID) {
        this.posterImageID = posterImageID;
    }

    public String getPosterImageURL() {
        return posterImageURL;
    }
    public void setPosterImageURL(String posterImageURL) {
        this.posterImageURL = posterImageURL;
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

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public EventStatus getStatus() {
        return status;
    }
    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public String getQrCodeImageID() {
        return qrCodeImageID;
    }
    public void setQrCodeImageID(String qrCodeImageID) {
        this.qrCodeImageID = qrCodeImageID;
    }

    public String getQrCodeImageURL() {
        return qrCodeImageURL;
    }
    public void setQrCodeImageURL(String qrCodeImageURL) {
        this.qrCodeImageURL = qrCodeImageURL;
    }

    // helpers
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
