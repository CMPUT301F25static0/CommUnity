package com.example.community;

public enum EntryStatus {
    WAITING, // in waiting list
    INVITED,  // selected by lottery
    ACCEPTED, // accepted invitation
    DECLINED, // declined invitation
    CANCELLED // left waiting list, left attendee list
}
