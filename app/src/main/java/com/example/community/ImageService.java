package com.example.community;

import com.google.android.gms.tasks.Task;

public class ImageService {
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;

    ImageService() {
        this.imageRepository = new ImageRepository();
        this.eventRepository = new EventRepository();
    }

    /**
     * Uploads event poster and updates the Event document
     * @param eventID The event ID
     * @param imageData The image bytes
     * @param uploadedBy User ID who is uploading the poster
     * @return Task that resolves to Image object with storagePath and download URL
     */
    public Task<Image> uploadEventPoster(String eventID, byte[] imageData, String uploadedBy) {
        // Validate input
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Image data cannot be null or empty");
        }

        // Construct storage path (Service knows business rules)
        String storagePath = "images/events/" + eventID + "/poster.jpg";

        // Repository does the heavy lifting (Storage + Firestore)
        return imageRepository.upload(imageData, storagePath, uploadedBy)
                .onSuccessTask(image -> {
                    // Update Event document with poster URL and imageID
                    return eventRepository.updateEventPoster(
                            eventID,
                            image.getImageURL(),
                            image.getImageID()
                    ).continueWith(task -> {
                        if (!task.isSuccessful()) {
                            // If Event update fails, consider cleanup
                            // For now, we let the image remain in case of retry
                            throw task.getException();
                        }
                        return image;
                    });
                });
    }

    /**
     * Deletes event poster from both Storage/Firestore and clears the Event document
     * @param eventID The event ID
     * @return Task that completes when both Storage/Firestore and Event are updated
     */
    public Task<Void> deleteEventPoster(String eventID) {
        // Get the Event to retrieve the posterImageID
        return eventRepository.getEventByEventID(eventID)
                .onSuccessTask(eventDoc -> {
                    if (!eventDoc.exists()) {
                        throw new IllegalArgumentException("Event not found: " + eventID);
                    }

                    Event event = eventDoc.toObject(Event.class);
                    String posterImageID = event.getPosterImageID();

                    if (posterImageID == null) {
                        // No poster to delete, just clear the Event fields
                        return eventRepository.clearEventPoster(eventID);
                    }

                    // Delete from both Storage and Firestore using imageID
                    return imageRepository.delete(posterImageID)
                            .continueWithTask(task -> {
                                // Clear Event poster fields regardless of deletion result
                                return eventRepository.clearEventPoster(eventID);
                            });
                });
    }

    /**
     * Generic helper method for uploading event-related images
     * Can be used by other services (like QRCodeService) for consistency
     * @param eventID The event ID
     * @param imageData The image bytes
     * @param filename The filename (e.g., "poster.jpg", "qrcode.png")
     * @param uploadedBy User ID who is uploading
     * @return Task that resolves to Image object
     */
    public Task<Image> uploadEventImage(String eventID, byte[] imageData,
                                        String filename, String uploadedBy) {
        String storagePath = "images/events/" + eventID + "/" + filename;
        return imageRepository.upload(imageData, storagePath, uploadedBy);
    }
}
