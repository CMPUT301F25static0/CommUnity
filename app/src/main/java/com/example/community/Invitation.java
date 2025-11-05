package com.example.community;

import java.time.LocalDateTime;

public class Invitation {
    private String invitationId;
    private String eventId;
    private String entrantId;
    private LocalDateTime issueDate; // Might remove; not needed
    private LocalDateTime expiryDate; // Might remove; not needed
    private EntryStatus status;

    public Invitation() { }

    public Invitation(String eventId, String entrantId) {
        this.invitationId = generateInvitationId();
        this.eventId = eventId;
        this.entrantId = entrantId;
        this.status = EntryStatus.INVITED;
    }

    private String generateInvitationId() {
        return "INV_" + eventId + entrantId;
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


    public EntryStatus getStatus() {
        return status;
    }
    public void setStatus(EntryStatus state) {
        this.status = state;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }
}
