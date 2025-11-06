package com.example.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

public class Event {

    @DocumentId
    private String eventID; // need to make a UUID to string helper
    private String title;
    private String description;
    private String organizerId;


    private String location;
    private String eventType;
    private Integer capacity;

    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;

    private String qrCodeImageURL;
    private String posterImageURL;
    private Boolean geoLocationEnabled;




    public Event() { }

    public Event(String eventID, String title, String description, String organizerId, String location,
                 String eventType, Integer capacity, LocalDateTime eventStartDate,
                 LocalDateTime eventEndDate, LocalDateTime registrationStart, LocalDateTime registrationEnd,
                 String qrCodeImageURL) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
        this.organizerId = organizerId;
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

    public String getOrganizerId() {
        return organizerId;
    }
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
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

}
