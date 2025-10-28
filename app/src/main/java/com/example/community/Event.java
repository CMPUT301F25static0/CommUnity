package com.example.community;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Event {
    private UUID eventID;
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

    private List<User> waitList;
    private List<User> attendeeList;
    private List<User> invitedList;
    private List<User> cancelledList;

    public Event(UUID eventID, String title, String description, User organizer, String location,
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

    public UUID getEventID() {
        return eventID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getOrganizer() {
        return organizer;
    }

    public String getLocation() {
        return location;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public LocalDateTime getEventStartDate() {
        return eventStartDate;
    }

    public LocalDateTime getEventEndDate() {
        return eventEndDate;
    }

    public LocalDateTime getRegistrationStart() {
        return registrationStart;
    }

    public LocalDateTime getRegistrationEnd() {
        return registrationEnd;
    }

    public String getQrCodeImageURL() {
        return qrCodeImageURL;
    }

    public List<User> getWaitList() {
        return waitList;
    }

    public List<User> getAttendeeList() {
        return attendeeList;
    }

    public List<User> getInvitedList() {
        return invitedList;
    }

    public List<User> getCancelledList() {
        return cancelledList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setEventStartDate(LocalDateTime eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public void setEventEndDate(LocalDateTime eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public void setRegistrationStart(LocalDateTime registrationStart) {
        this.registrationStart = registrationStart;
    }

    public void setRegistrationEnd(LocalDateTime registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public void setQrCodeImageURL(String qrCodeImageURL) {
        this.qrCodeImageURL = qrCodeImageURL;
    }

    public void setWaitList(List<User> waitList) {
        this.waitList = waitList;
    }

    public void setAttendeeList(List<User> attendeeList) {
        this.attendeeList = attendeeList;
    }

    public void setInvitedList(List<User> invitedList) {
        this.invitedList = invitedList;
    }

    public void setCancelledList(List<User> cancelledList) {
        this.cancelledList = cancelledList;
    }
}
