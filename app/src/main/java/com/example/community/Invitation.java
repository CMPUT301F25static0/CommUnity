package com.example.community;

import java.time.LocalDateTime;

public class Invitation {
    private String invitationID;
    private String eventId;
    private String entrantId;
    private LocalDateTime issueDate;
    private LocalDateTime expiryDate;
    private WaitingListEntry.EntryStatus state;

    public Invitation() { }

    public Invitation(String invitationID, String eventId, String entrantId, LocalDateTime issuedTime,
                      LocalDateTime expiryTime, WaitingListEntry.EntryStatus state) {
        this.invitationID = invitationID;
        this.eventId = eventId;
        this.entrantId = entrantId;
        this.issueDate = issuedTime;
        this.expiryDate = expiryTime;
        this.state = state;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEntrantId() {
        return entrantId;
    }
    public void setEntrantId(String entrantId) {
        this.entrantId = entrantId;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }


    public WaitingListEntry.EntryStatus getState() {
        return state;
    }
    public void setState(WaitingListEntry.EntryStatus state) {
        this.state = state;
    }

    public String getInvitationID() {
        return invitationID;
    }

    public void setInvitationID(String invitationID) {
        this.invitationID = invitationID;
    }
}
