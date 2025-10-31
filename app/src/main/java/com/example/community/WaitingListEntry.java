package com.example.community;

public class WaitingListEntry {
    public enum EntryStatus {
        WAITING, // in waiting list
        INVITED,  // selected by lottery
        ACCEPTED, // accepted invitation
        DECLINED, // declined invitation
        CANCELLED // left waiting list, left attendee list
    }
    private String entryID;
    private String eventID;
    private String userID;
    private  EntryStatus status = EntryStatus.WAITING;

    public WaitingListEntry() { }

    public WaitingListEntry(String entryID, String eventID, String userID) {
        this.entryID = entryID;
        this.eventID = eventID;
        this.userID = userID;
    }

    public String getEntryID() {
        return entryID;
    }
    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public EntryStatus getStatus() {
        return status;
    }
    public void setStatus(EntryStatus status) {
        this.status = status;
    }
}
