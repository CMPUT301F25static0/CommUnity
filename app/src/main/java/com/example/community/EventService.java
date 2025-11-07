package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.List;

public class EventService {
    private final EventRepository eventRepository;
    private final WaitlistRepository waitlistRepository;
    private final QRCodeService qrCodeService;
    private final ImageService imageService;
    private final UserRepository userRepository;

    public EventService() {
        this.eventRepository = new EventRepository();
        this.waitlistRepository = new WaitlistRepository();
        this.qrCodeService = new QRCodeService();
        this.imageService = new ImageService();
        this.userRepository = new UserRepository();
    }

    public Task<String> createEvent(String organizerID, String title, String description,
                                    Integer maxCapacity, String eventStartDate,
                                    String eventEndDate) {

        Event e = new Event();
        e.setEventID(java.util.UUID.randomUUID().toString());
        e.setOrganizerID(organizerID);
        e.setTitle(title);
        e.setDescription(description);
        e.setMaxCapacity(maxCapacity);
        e.setCurrentCapacity(0);
        e.setEventStartDate(eventStartDate);
        e.setEventEndDate(eventEndDate);
        e.setStatus(EventStatus.OPEN);

        return eventRepository.create(e).continueWith(t -> e.getEventID());
    }

    public Task<Void> updateEvent(String organizerID, Event patch) {
        return eventRepository.update(patch);
    }

    public Task<Void> publishEvent(String organizerID, String eventID) {
        return eventRepository.getByID(eventID).continueWithTask(task -> {
            Event event = task.getResult();
            if (event == null) {
                return Tasks.forException(new IllegalArgumentException("Event not found"));
            }
            if (!event.getOrganizerID().equals(organizerID)) {
                return Tasks.forException(new SecurityException("Not authorized"));
            }
            event.setStatus(EventStatus.OPEN);
            return eventRepository.update(event);
        });
    }

    public Task<Void> cancelEvent(String organizerID, String eventID) {
        return eventRepository.getByID(eventID).continueWithTask(task -> {
            Event event = task.getResult();
            if (event == null) {
                return Tasks.forException(new IllegalArgumentException("Event not found"));
            }
            if (!event.getOrganizerID().equals(organizerID)) {
                return Tasks.forException(new SecurityException("Not authorized"));
            }
            event.setStatus(EventStatus.CANCELLED);
            return eventRepository.update(event);
        });
    }

    public Task<List<Event>> listEventsByOrganizer(String organizerID, int limit, String startAfterID) {
        return eventRepository.listEventsByOrganizer(organizerID, limit, startAfterID);
    }

    public Task<String> setPoster(String organizerID, String eventID, byte[] imageData, String uploadedBy) {
        return imageService.uploadEventPoster(eventID, imageData, uploadedBy).continueWith(t -> t.getResult().getImageURL());
    }

    public Task<String> refreshEventQR(String organizerID, String eventID) {
        return qrCodeService.generateAndUploadQRCode(eventID, organizerID).continueWith(t -> t.getResult().getImageURL());
    }

    public Task<List<Event>> listUpcoming(String fromDate, String toDate, List<String> tags) {
        return eventRepository.listUpcoming(fromDate, toDate, tags, 50, null).continueWith(task -> {
            List<Event> all = task.getResult();
            List<Event> openOnly = new java.util.ArrayList<>();
            for (Event e : all) {
                if (e != null && e.getStatus() == EventStatus.OPEN) {
                    openOnly.add(e);
                }
            }
            return openOnly;
        });
    }

    public Task<List<Event>> listJoinable(String userID, String fromDate, String toDate, List<String> tags) {
        return listUpcoming(fromDate, toDate, tags)   // already filtered to OPEN
                .onSuccessTask(events -> {
                    List<Task<Boolean>> checks = new java.util.ArrayList<>();
                    for (Event e : events) {
                        checks.add(waitlistRepository.getByID(e.getEventID(), userID).continueWith(t -> t.getResult() == null));
                    }
                    return Tasks.whenAllSuccess(checks).continueWith(t -> {
                        List<?> results = t.getResult();
                        List<Event> joinable = new java.util.ArrayList<>();
                        for (int i = 0; i < events.size(); i++) {
                            boolean canJoin = (Boolean) results.get(i);
                            if (canJoin) {
                                joinable.add(events.get(i));
                            }
                        }
                        return joinable;
                    });
                });
    }

    public Task<List<Event>> listJoinableByInterests(String userID, String fromDate, String toDate) {
        return listJoinable(userID, fromDate, toDate, null);
    }

    public Task<Event> getEvent(String eventID) {
        return eventRepository.getByID(eventID);
    }

    public Task<String> getEventQRCode(String organizerID, String eventID) {
        return eventRepository.getByID(eventID).continueWith(task -> {
            Event event = task.getResult();
            if (event == null) {
                throw new IllegalArgumentException("Event not found");
            }
            return event.getQRCodeImageURL();
        });
    }


    public Task<List<User>> getAttendees(String eventID) {
        return waitlistRepository
                .listByEventAndStatus(eventID, EntryStatus.ACCEPTED)
                .onSuccessTask(entries -> {
                    java.util.List<Task<User>> reads = new java.util.ArrayList<>();
                    for (WaitingListEntry e : entries) {
                        reads.add(userRepository.getByID(e.getUserID()));
                    }
                    return com.google.android.gms.tasks.Tasks.whenAllSuccess(reads)
                            .continueWith(t -> {
                                java.util.List<?> results = t.getResult();
                                java.util.List<User> users = new java.util.ArrayList<>();
                                for (Object o : results) {
                                    User u = (User) o;
                                    if (u != null) users.add(u);
                                }
                                return users;
                            });
                });
    }


    // public Task<String> exportAttendeesCSV(String organizerID, String eventID) {}
}
