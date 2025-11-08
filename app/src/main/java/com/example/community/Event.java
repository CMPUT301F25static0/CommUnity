package com.example.community;

import com.google.firebase.firestore.DocumentId;

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
    private Integer waitlistCapacity;
    private Integer currentWaitingListSize;
    private List<String> tags = new ArrayList<>();
    private EventStatus status = EventStatus.DRAFT;

    private String eventStartDate;
    private String eventEndDate;
    private String registrationStart;
    private String registrationEnd;

    private String QRCodeImageID;
    private String QRCodeImageURL;
    private String posterImageID;
    private String posterImageURL;

    private List<String> waitListUserIDs = new ArrayList<>();
    private List<String> attendeeListUserIDs = new ArrayList<>();
    private List<String> invitedListUserIDs = new ArrayList<>();
    private List<String> cancelledListUserIDs = new ArrayList<>();

    public Event() {
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

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(String registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public String getQRCodeImageID() {
        return QRCodeImageID;
    }

    public void setQRCodeImageID(String QRCodeImageID) {
        this.QRCodeImageID = QRCodeImageID;
    }

    public String getQRCodeImageURL() {
        return QRCodeImageURL;
    }

    public void setQRCodeImageURL(String QRCodeImageURL) {
        this.QRCodeImageURL = QRCodeImageURL;
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

    public Integer getWaitlistCapacity() {
        return waitlistCapacity;
    }

    public void setWaitlistCapacity(Integer waitlistCapacity) {
        this.waitlistCapacity = waitlistCapacity;
    }

    public Integer getCurrentWaitingListSize() {
        return currentWaitingListSize;
    }

    public void setCurrentWaitingListSize(Integer size) {
        this.currentWaitingListSize = size;
    }



    // helpers
    public void incrementWaitingListSize() {
        if (currentWaitingListSize == null) {
            currentWaitingListSize = 0;
        }
        currentWaitingListSize++;
    }

    public void decrementWaitingListSize() {
        if (currentWaitingListSize != null && currentWaitingListSize > 0) {
            currentWaitingListSize--;
        }
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
