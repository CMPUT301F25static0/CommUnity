package com.example.community;

import java.util.UUID;

public class WaitingListEntry {
    public enum EntryStatus {
        WAITING, // in waiting list
        INVITED,  // selected by lottery
        ACCEPTED, // accepted invitation
        DECLINED, // declined invitation
        CANCELLED // left waiting list, left attendee list
    }
    private UUID entryID;
    private UUID eventID;
    private UUID userID;

    public WaitingListEntry(UUID entryID, UUID eventID, UUID userID) {
        this.entryID = entryID;
        this.eventID = eventID;
        this.userID = userID;
    }
}
