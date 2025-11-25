package com.example.community;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

/**
 * Service layer for notification operations.
 * Handles sending notifications to users about events.
 */
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final WaitlistRepository waitlistRepository;

    /**
     * Creates a new NotificationService instance.
     * Initializes required repositories.
     */
    public NotificationService() {
        this.notificationRepository = new NotificationRepository();
        this.waitlistRepository = new WaitlistRepository();
    }

    /**
     * Sends notifications to users who were selected for an event.
     * US 02.05.01, US 01.04.01,
     *
     * @param eventID ID of the event
     * @return task that completes when notifications are sent
     */
    public Task<Void> notifyWinners(String eventID, List<WaitingListEntry> lotteryWinners) {
        List<String> recipientIDs = new ArrayList<>();

        for (WaitingListEntry e : lotteryWinners) {
            recipientIDs.add(e.getUserID());
        }
        return notificationRepository.createMany(
                eventID,
                recipientIDs,
                NotificationType.WIN,
                "You were selected for this event! Please accept or decline the invitation."
        );
    }

    /**
     * Sends notifications to users who were not selected for an event.
     * US 01.04.02
     *
     * @param eventID ID of the event
     * @return task that completes when notifications are sent
     */
    public Task<Void> notifyLosers(String eventID, List<WaitingListEntry> lotteryLosers) {
        List<String> recipientIDS = new ArrayList<>();
        for (WaitingListEntry e : lotteryLosers) {
            recipientIDS.add(e.getUserID());
        }
        return notificationRepository.createMany(
                eventID,
                recipientIDS,
                NotificationType.LOSE,
                "The lottery was ran but you were not selected at this time."
        );
    }

    /**
     * Sends a broadcast message to all invited users for an event.
     * US 02.07.02
     *
     * @param organizerID ID of the organizer
     * @param eventID ID of the event
     * @param message message to broadcast
     * @return task that completes when notifications are sent
     */
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

    /**
     * Sends a broadcast message to everyone on an event's waitlist.
     * US 02.07.01
     * @param organizerID ID of the organizer
     * @param eventID ID of the event
     * @param message message to broadcast
     * @return task that completes when notifications are sent
     */
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

    /**
     * Sends an informational notification to a single user.
     * 02.07.x ?
     *
     * @param eventID ID of the related event
     * @param userID ID of the user to notify
     * @param message notification message
     * @return task that completes when notification is sent
     */
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

    /**
     * Lists all notifications for a user with pagination.
     * US 01.04.01, US 01.04.02, US 01.04.03
     *
     * @param userID ID of the user
     * @param limit maximum number to return
     * @param startAfterID ID to start pagination after
     * @return task containing list of notifications
     */
    public Task<java.util.List<Notification>> listUserNotification(String userID, int limit,
                                                                   String startAfterID) {
        return notificationRepository.listNotificationsByRecipient(userID, limit, startAfterID);
    }
}

