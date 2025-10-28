package com.example.community;

import java.time.LocalDateTime;

public class Invitation {
    String eventId;
    String entrantId;
    LocalDateTime issueDate;
    LocalDateTime expiryDate;
    WaitingListEntry.EntryStatus state;

    public Invitation(String eventId, String entrantId, LocalDateTime issuedTime,
                      LocalDateTime expiryTime, WaitingListEntry.EntryStatus state) {
        this.eventId = eventId;
        this.entrantId = entrantId;
        this.issueDate = issuedTime;
        this.expiryDate = expiryTime;
        this.state = state;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEntrantId() {
        return entrantId;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public WaitingListEntry.EntryStatus getState() {
        return state;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setState(WaitingListEntry.EntryStatus state) {
        this.state = state;
    }
}
