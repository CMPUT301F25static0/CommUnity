package com.example.community;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.List;

/**
 * Service layer for event operations.
 * Handles business logic for creating, updating, and managing events.
 */
public class EventService {
    private final EventRepository eventRepository;
    private final WaitlistRepository waitlistRepository;
    private final QRCodeService qrCodeService;
    private final ImageService imageService;
    private final UserRepository userRepository;

    /**
     * Creates a new EventService instance.
     * Initializes all required repositories and services.
     */
    public EventService() {
        this.eventRepository = new EventRepository();
        this.waitlistRepository = new WaitlistRepository();
        this.qrCodeService = new QRCodeService();
        this.imageService = new ImageService();
        this.userRepository = new UserRepository();
    }

    /**
     * Creates a new event and adds it to the organizer's created events list.
     *
     * @param organizerID ID of the event organizer
     * @param title       event title
     * @param description event description
     * @param maxCapacity maximum number of attendees
     * @param startDate   event start date
     * @param endDate     event end date
     * @return task containing the new event ID
     */
    public Task<String> createEvent(String organizerID, String title, String description,
                                    Integer maxCapacity, String startDate, String endDate, Integer maxWaitingListSize,
                                    String regStart, String regEnd)
    {
        Event e = new Event();
        e.setEventID(java.util.UUID.randomUUID().toString());
        e.setOrganizerID(organizerID);
        e.setTitle(title);
        e.setDescription(description);
        e.setMaxCapacity(maxCapacity);
        e.setCurrentCapacity(0);
        e.setEventStartDate(startDate);
        e.setEventEndDate(endDate);
        e.setStatus(EventStatus.OPEN);
        e.setRegistrationStart(regStart);
        e.setRegistrationEnd(regEnd);
        if (maxWaitingListSize != null ) {
            e.setWaitlistCapacity(maxWaitingListSize);
        }

        return eventRepository.create(e)
                .continueWithTask(t -> {
                    if (!t.isSuccessful()) throw t.getException();
                    final String eventID = e.getEventID();

                    // append to organizer.eventsCreatedIDs
                    return userRepository
                            .getByUserID(organizerID)
                            .continueWithTask(ut -> {
                                User u = ut.getResult();
                                if (u == null) {
                                    return Tasks
                                            .forException(new IllegalArgumentException("Organizer not found"));
                                }
                                if (!u.hasEventCreated(eventID)) {
                                    u.addEventCreated(eventID);
                                    return userRepository
                                            .update(u)
                                            .continueWith(tt -> eventID);
                                }
                                // already recorded; just return id
                                return Tasks.forResult(eventID);
                            });
                });
    }

    /**
     * Updates an existing event with new information.
     *
     * @param organizerID ID of the organizer making the update
     * @param patch       event object with updated fields
     * @return task that completes when update finishes
     */
    public Task<Void> updateEvent(String organizerID, Event patch) {
        return eventRepository.update(patch);
    }

    /**
     * Changes an event status to open/published.
     *
     * @param organizerID ID of the organizer
     * @param eventID     ID of the event to publish
     * @return task that completes when status is updated
     */
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

    /**
     * Changes an event status to cancelled.
     *
     * @param organizerID ID of the organizer
     * @param eventID     ID of the event to cancel
     * @return task that completes when status is updated
     */
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

    /**
     * Lists all events created by an organizer with pagination.
     *
     * @param organizerID  ID of the organizer
     * @param limit        maximum number of events to return
     * @param startAfterID ID to start pagination after
     * @return task containing list of events
     */
    public Task<List<Event>> listEventsByOrganizer(String organizerID, int limit, String startAfterID) {
        return eventRepository.listEventsByOrganizer(organizerID, limit, startAfterID);
    }

    /**
     * Uploads and sets a poster image for an event.
     *
     * @param organizerID ID of the organizer
     * @param eventID     ID of the event
     * @param imageData   raw image bytes
     * @param uploadedBy  ID of user uploading the image
     * @return task containing the image URL
     */
    public Task<String> setPoster(String organizerID, String eventID, byte[] imageData, String uploadedBy) {
        return imageService.uploadEventPoster(eventID, imageData, uploadedBy).continueWith(t -> t.getResult().getImageURL());
    }

    /**
     * Generates a new QR code for an event.
     *
     * @param organizerID ID of the organizer
     * @param eventID     ID of the event
     * @return task containing the QR code image URL
     */
    public Task<String> refreshEventQR(String organizerID, String eventID) {
        return qrCodeService.generateAndUploadQRCode(eventID, organizerID).continueWith(t -> t.getResult().getImageURL());
    }

    /**
     * Lists all upcoming open events within a date range.
     *
     * @param fromDate earliest event start date
     * @param toDate   latest event start date
     * @param tags     optional list of tags to filter by
     * @return task containing list of open events
     */
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

    /**
     * Lists events a user can join (not already on waitlist).
     *
     * @param userID   ID of the user
     * @param fromDate earliest event start date
     * @param toDate   latest event start date
     * @param tags     optional list of tags to filter by
     * @return task containing list of joinable events
     */
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

    /**
     * Lists joinable events filtered by user interests.
     *
     * @param userID   ID of the user
     * @param fromDate earliest event start date
     * @param toDate   latest event start date
     * @return task containing list of matching events
     */
    public Task<List<Event>> listJoinableByInterests(String userID, String fromDate, String toDate) {
        return listJoinable(userID, fromDate, toDate, null);
    }

    /**
     * Retrieves a single event by ID.
     *
     * @param eventID ID of the event
     * @return task containing the event
     */
    public Task<Event> getEvent(String eventID) {
        return eventRepository.getByID(eventID);
    }

    /**
     * Gets the QR code URL for an event.
     *
     * @param organizerID ID of the organizer
     * @param eventID     ID of the event
     * @return task containing the QR code URL
     */
    public Task<String> getEventQRCode(String organizerID, String eventID) {
        return eventRepository.getByID(eventID).continueWith(task -> {
            Event event = task.getResult();
            if (event == null) {
                throw new IllegalArgumentException("Event not found");
            }
            return event.getQRCodeImageURL();
        });
    }

    /**
     * Gets all users who have accepted invitations to an event.
     *
     * @param eventID ID of the event
     * @return task containing list of attendee users
     */
    public Task<List<User>> getAttendees(String eventID) {
        return waitlistRepository
                .listByEventAndStatus(eventID, EntryStatus.ACCEPTED)
                .onSuccessTask(entries -> {
                    java.util.List<Task<User>> reads = new java.util.ArrayList<>();
                    for (WaitingListEntry e : entries) {
                        reads.add(userRepository.getByUserID(e.getUserID()));
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
