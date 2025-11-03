package com.example.community;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.sql.Timestamp;

public class WaitingListEntryTest {

    @Test
    public void testHasStatus() {
        WaitingListEntry entry = new WaitingListEntry();
        entry.setStatus(WaitingListEntry.EntryStatus.INVITED);

        assertTrue(entry.hasStatus(WaitingListEntry.EntryStatus.INVITED));
        assertFalse(entry.hasStatus(WaitingListEntry.EntryStatus.ACCEPTED));
    }

    @Test
    public void testMarkAsJoined() {
        WaitingListEntry entry = new WaitingListEntry();
        entry.markAsJoined();
        assertEquals(WaitingListEntry.EntryStatus.WAITING, entry.getStatus());
    }

    @Test
    public void testMarkAsAccepted() {
        WaitingListEntry entry = new WaitingListEntry();
        entry.markAsAccepted();
        assertEquals(WaitingListEntry.EntryStatus.ACCEPTED, entry.getStatus());
    }

    @Test
    public void testMarkAsDeclined() {
        WaitingListEntry entry = new WaitingListEntry();
        entry.markAsDeclined();
        assertEquals(WaitingListEntry.EntryStatus.DECLINED, entry.getStatus());
    }

    @Test
    public void testMarkAsCancelled() {
        WaitingListEntry entry = new WaitingListEntry();
        entry.markAsCancelled();
        assertEquals(WaitingListEntry.EntryStatus.CANCELLED, entry.getStatus());
    }

}


