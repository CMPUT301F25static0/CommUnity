package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WaitingListEntryService {

    private WaitlistRepository waitlistRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    WaitingListEntryService() {
        waitlistRepository = new WaitlistRepository();
        this.eventRepository = new EventRepository();
        this.userRepository = new UserRepository();
    }

    public Task<Void> join(String userID, String eventID) {
        return waitlistRepository.getByID(eventID, userID).continueWithTask(task -> {
            WaitingListEntry existing = task.getResult();
            if (existing != null) {
                return Tasks.forException(new IllegalArgumentException("Already on waitlist"));
            }

            String entryID = UUID.randomUUID().toString();
            WaitingListEntry entry = new WaitingListEntry(entryID, eventID, userID);
            entry.markAsJoined();

            return waitlistRepository.create(entry);
        });
    }

    public Task<Void> leave(String userID, String eventID) {
        return waitlistRepository.getByID(eventID, userID).continueWithTask(task -> {
            WaitingListEntry entry = task.getResult();
            if (entry == null) {
                return Tasks.forException(new IllegalArgumentException("Not on waitlist"));
            }
            if (entry.hasStatus(EntryStatus.ACCEPTED)) {
                return Tasks.forException(new IllegalStateException("Cannot leave after accepting"));
            }

            entry.markAsCancelled();

            return waitlistRepository.delete(eventID, userID);
        });
    }

    public Task<Void> invite(String organizerID, String eventID, String userID) {
        return waitlistRepository.getByID(eventID, userID).continueWithTask(task -> {
            WaitingListEntry entry = task.getResult();
            if (entry == null) {
                return Tasks.forException(new IllegalArgumentException("Entry not found"));
            }
            entry.setStatus(EntryStatus.INVITED);
            entry.setInvitedAt(Timestamp.now());
            return waitlistRepository.update(entry);
        });
    }

    public Task<Void> acceptInvite(String userID, String eventID) {
        return waitlistRepository.getByID(eventID, userID).continueWithTask(task -> {
            WaitingListEntry entry = task.getResult();
            if (entry == null) {
                return Tasks.forException(new IllegalArgumentException("Not on waitlist"));
            }
            if (!entry.hasStatus(EntryStatus.INVITED)) {
                return Tasks.forException(new IllegalStateException("Invite not pending"));
            }

            return eventRepository.getByID(eventID).continueWithTask(eventTask -> {
                Event event = eventTask.getResult();
                if (event == null) {
                    return Tasks.forException(new IllegalArgumentException("Event not found"));
                }

                Integer maxCap = event.getMaxCapacity();
                Integer currCap = event.getCurrentCapacity();

                if (maxCap == null) {
                    return Tasks.forException(new IllegalStateException("Event max capacity not set"));
                }
                if (currCap == null) {
                    currCap = 0;
                }
                if (currCap >= maxCap) {
                    return Tasks.forException(new IllegalStateException("Event is full"));
                }

                // update event capacity
                event.setCurrentCapacity(currCap + 1);

                // update entry
                entry.markAsAccepted();

                return Tasks.whenAll(eventRepository.update(event), waitlistRepository.update(entry));
            });
        });
    }

    public Task<Void> declineInvite(String userID, String eventID) {
        return waitlistRepository.getByID(eventID, userID).continueWithTask(task -> {
            WaitingListEntry entry = task.getResult();
            if (entry == null) {
                return Tasks.forException(new IllegalArgumentException("Not on waitlist"));
            }
            if (!entry.hasStatus(EntryStatus.INVITED)) {
                return Tasks.forException(new IllegalStateException("Invite not pending"));
            }

            entry.markAsDeclined();
            return waitlistRepository.update(entry);
        });
    }

    public Task<List<WaitingListEntry>> getWaitlistEntries(String eventID) {
        return waitlistRepository.listByEvent(eventID);
    }

    public Task<List<WaitingListEntry>> getInvitedList(String eventID) {
        return waitlistRepository.listByEventAndStatus(eventID, EntryStatus.INVITED);
    }

    public Task<List<WaitingListEntry>> getAcceptedList(String eventID) {
        return waitlistRepository.listByEventAndStatus(eventID, EntryStatus.ACCEPTED);
    }

    public Task<Long> getWaitlistSize(String eventID) {
        return waitlistRepository.countByEvent(eventID);
    }

    public Task<Map<EntryStatus, Long>> getWaitlistCounts(String eventID) {
        return waitlistRepository.countsByEventGrouped(eventID);
    }

    public Task<List<WaitingListEntry>> getHistory(String userID) {
        return waitlistRepository.listByUser(userID);
    }
}

