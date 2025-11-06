package com.example.community;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final WaitlistRepository waitlistRepository;

    public NotificationService() {
        this.notificationRepository = new NotificationRepository();
        this.waitlistRepository = new WaitlistRepository();
    }

    // US 02.05.01, US 01.04.01,
    public Task<Void> notifyWinners(String organizerID, String eventID) {
        return waitlistRepository.listByEventAndStatus(eventID, EntryStatus.ACCEPTED)
                .onSuccessTask(entries -> {
            java.util.List<String> recipientIDs = new java.util.ArrayList<>();
            for (WaitingListEntry e : entries) {
                recipientIDs.add(e.getUserID());
            }
            return notificationRepository.createMany(
                    eventID,
                    recipientIDs,
                    NotificationType.WIN,
                    "You were selected for this event! Accept or Decline invitation?");
        });
    }

    // US 01.04.02
    public Task<Void> notifyLosers(String organizerID, String eventID) {
        return waitlistRepository.listByEventAndStatus(eventID, EntryStatus.DECLINED)
                .onSuccessTask(entries -> {
            java.util.List<String> recipientIDs = new java.util.ArrayList<>();
            for (WaitingListEntry e : entries) {
                recipientIDs.add(e.getUserID());
            }
            return notificationRepository.createMany(eventID,
                    recipientIDs,
                    NotificationType.LOSE,
                    "You were not selected this time.");
        });
    }

    // US 02.07.02
    public Task<Void> broadcastToInvited(String organizerID, String eventID, String message) {
        return waitlistRepository.listByEventAndStatus(eventID, EntryStatus.INVITED).
                onSuccessTask(entries -> {
            java.util.List<String> recipientIDs = new java.util.ArrayList<>();
            for (WaitingListEntry e : entries) {
                recipientIDs.add(e.getUserID());
            }
            return notificationRepository.createMany(
                    eventID,
                    recipientIDs,
                    NotificationType.BROADCAST, message);
        });
    }

    // US 02.07.01
    public Task<Void> broadcastToWaitlist(String organizerID, String eventID, String message) {
        return waitlistRepository.listByEvent(eventID).onSuccessTask(entries -> {
            java.util.List<String> recipientIDs = new java.util.ArrayList<>();
            for (WaitingListEntry e : entries) {
                recipientIDs.add(e.getUserID());
            }
            return notificationRepository.createMany(
                    eventID,
                    recipientIDs,
                    NotificationType.BROADCAST, message);
        });
    }

    // 02.07.x ?
    public Task<Void> sendInfoToUser(String eventID, String userID, String message) {
        Notification n = new Notification();
        n.setNotificationID(java.util.UUID.randomUUID().toString());
        n.setRecipientID(userID);
        n.setEventID(eventID);
        n.setType(NotificationType.INFO);
        n.setMessage(message);
        n.setIssueDate(System.currentTimeMillis());
        return notificationRepository.create(n);
    }

    // US 01.04.01, US 01.04.02, US 01.04.03
    public Task<java.util.List<Notification>> listUserNotification(String userID, int limit,
                                                                   String startAfterID) {
        return notificationRepository.listNotificationsByRecipient(userID, limit, startAfterID);
    }
}

