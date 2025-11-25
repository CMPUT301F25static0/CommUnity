package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotteryService {
    private final String TAG = "LotteryService";

    private WaitlistRepository waitlistRepository;
    private EventRepository eventRepository;

    private NotificationService notificationService;
    private WaitingListEntryService waitingListEntryService;

    public LotteryService() {
        this.waitlistRepository = new WaitlistRepository();
        this.eventRepository = new EventRepository();
        this.notificationService = new NotificationService();
        this.waitingListEntryService = new WaitingListEntryService();
    }

    public Task<Void> runLottery(String organizerID, String eventID) {
        return eventRepository.getByID(eventID)
                .continueWithTask(eventTask -> {
                    Event event = eventTask.getResult();
                    if (event == null) {
                        return Tasks.forException(new IllegalArgumentException("Event not found"));
                    }
                    if (!event.getOrganizerID().equals(organizerID)) {
                        return Tasks.forException(new IllegalArgumentException("User is not organizer of event"));
                    }

                    Integer maxCapacity = event.getMaxCapacity();
                    Integer currentCapacity = event.getCurrentCapacity();
                    if (maxCapacity == null || currentCapacity == null){
                        return Tasks.forException(new IllegalArgumentException("Event capacity is not set"));
                    }

                    int availableSlots = maxCapacity - currentCapacity;
                    if (availableSlots <= 0) {
                        return Tasks.forException(new IllegalArgumentException("No available slots for event"));
                    }

                    return waitlistRepository.listByEventAndStatus(eventID, EntryStatus.WAITING)
                            .continueWithTask(entriesTask -> {
                                if (!entriesTask.isSuccessful()) {
                                    throw entriesTask.getException();
                                }
                                List<WaitingListEntry> waitingEntries = entriesTask.getResult();
                                if (waitingEntries == null || waitingEntries.isEmpty()) {
                                    return Tasks.forException(new IllegalArgumentException("No users on waitlist"));
                                }

                                int slotsToFill = Math.min(availableSlots, waitingEntries.size());
                                List<WaitingListEntry> lotteryWinners = selectLotteryWinners(waitingEntries, slotsToFill);
                                List<WaitingListEntry> lotteryLosers = getLotteryLosers(waitingEntries, lotteryWinners);

                                return markAsInvited(organizerID, eventID, lotteryWinners)
                                        .continueWithTask(inviteTask -> {
                                            if (!inviteTask.isSuccessful()) {
                                                throw inviteTask.getException();
                                            }
                                            return sendNotifications(lotteryWinners, lotteryLosers, eventID);
                                        });
                            });
                });
    }

    private <T> List<T> selectLotteryWinners(List<T> entriesList, int slotsToFill) {
        if (slotsToFill >= entriesList.size()) {
            return new ArrayList<>(entriesList);
        }

        List<T> copy = new ArrayList<>(entriesList);
        Random random = new Random();

        for (int i = 0; i < slotsToFill; i++) {
            int randomIndex = i + random.nextInt(copy.size() - i);
            T temp = copy.get(i);
            copy.set(i, copy.get(randomIndex));
            copy.set(randomIndex, temp);
        }

        return copy.subList(0, slotsToFill);
    }

    private List<WaitingListEntry> getLotteryLosers(List<WaitingListEntry> waitingEntries, List<WaitingListEntry> lotteryWinners) {
        List<WaitingListEntry> losers = new ArrayList<>(waitingEntries);
        losers.removeAll(lotteryWinners);
        return losers;
    }

    private Task<Void> markAsInvited(String organizerID, String eventID, List<WaitingListEntry> lotteryWinners) {
        List<Task<Void>> inviteTasks = new ArrayList<>();
        for (WaitingListEntry entry : lotteryWinners) {
            inviteTasks.add(waitingListEntryService.invite(organizerID, eventID, entry.getUserID()));
        }
        return Tasks.whenAll(inviteTasks);
    }

    private Task<Void> sendNotifications(List<WaitingListEntry> lotteryWinners,
                                         List<WaitingListEntry> lotteryLosers,
                                         String eventID) {
        List<Task<Void>> notificationTasks = new ArrayList<>();
        notificationTasks.add(notificationService.notifyWinners(eventID, lotteryWinners));
        notificationTasks.add(notificationService.notifyLosers(eventID, lotteryLosers));
        return Tasks.whenAll(notificationTasks);
    }


}
