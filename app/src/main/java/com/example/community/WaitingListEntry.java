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

    public WaitingListEntry(String entryID, String eventID, String userID) {
        this.entryID = entryID;
        this.eventID = eventID;
        this.userID = userID;
    }

    public String getEntryID() {
        return entryID;
    }

    public String getEventID() {
        return eventID;
    }

    public String getUserID() {
        return userID;
    }

    public EntryStatus getStatus() {
        return status;
    }
}
