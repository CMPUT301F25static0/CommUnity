package com.example.community.Screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.UserService;
import com.example.community.WaitingListEntry;
import com.example.community.WaitingListEntryService;

/**
 * Fragment for displaying detailed information about an event to an entrant.
 * Provides options to join or leave the event waitlist.
 */
public class EntrantEventDescriptionFragment extends Fragment {

    public static final String TAG = "EventDescriptionFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private Event currentEvent;
    private WaitingListEntryService waitingListEntryService;
    private UserService userService;
    private EventService eventService;
    private String currentEntrantId;

    private TextView eventTitle, eventDescription, eventLocation, eventDates,
            registrationDates, capacity, organizerUsername, organizerEmail,
            organizerPhone, waitlistCapacity;
    private Button waitlistButton, backButton;

    /**
     * Creates a new instance of this fragment with the specified event ID.
     *
     * @param eventId The ID of the event to display
     * @return A new instance of EntrantEventDescriptionFragment
     */
    public static EntrantEventDescriptionFragment newInstance(String eventId) {
        EntrantEventDescriptionFragment fragment = new EntrantEventDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater           LayoutInflater object to inflate views
     * @param container          Parent container for the fragment
     * @param savedInstanceState Saved instance state bundle
     * @return The inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.entrant_event_description, container, false);
    }

    /**
     * Called after the view has been created. Initializes services, binds UI elements,
     * loads event details, and sets up click listeners.
     *
     * @param view               The fragment's view
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waitingListEntryService = new WaitingListEntryService();
        eventService = new EventService();
        userService = new UserService();

        // Get current entrant ID from device token
        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> currentEntrantId = userId);

        // Bind UI elements
        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.eventDescription);
        eventLocation = view.findViewById(R.id.eventLocation);
        eventDates = view.findViewById(R.id.eventDates);
        registrationDates = view.findViewById(R.id.registrationDates);
        capacity = view.findViewById(R.id.capacity);
        organizerUsername = view.findViewById(R.id.eventOrganizerName);
        organizerEmail = view.findViewById(R.id.eventOrganizerEmail);
        organizerPhone = view.findViewById(R.id.eventOrganizerPhone);
        waitlistCapacity = view.findViewById(R.id.waitlistCount);
        waitlistButton = view.findViewById(R.id.waitlistButton);
        backButton = view.findViewById(R.id.backButton);

        loadEventDetails();

        // Back button navigates up
        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantEventDescriptionFragment.this)
                    .navigateUp();
        });
    }

    /**
     * Loads event details from the {@link EventService} and updates UI fields.
     * Also loads waitlist and organizer information.
     */
    private void loadEventDetails() {
        String eventId = getArguments().getString(ARG_EVENT_ID);
        eventService.getEvent(eventId)
                .addOnSuccessListener(event -> {
                    currentEvent = event;
                    eventTitle.setText(event.getTitle());
                    eventDescription.setText(event.getDescription());
                    eventDates.setText(String.format("Event Dates: %s - %s",
                            event.getEventStartDate(), event.getEventEndDate()));
                    registrationDates.setText(String.format("Registration Period: %s - %s",
                            event.getRegistrationStart(), event.getEventEndDate()));
                    capacity.setText(String.format("Capacity: %d/%d",
                            event.getCurrentCapacity(), event.getMaxCapacity()));
                    checkWaitlistStatus();
                    loadOrganizerDetails(event.getOrganizerID());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load event details", e);
                    Toast.makeText(getContext(), "Failed to load event details", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Loads the organizer's details from {@link UserService} and updates UI fields.
     *
     * @param organizerID The ID of the event organizer
     */
    private void loadOrganizerDetails(String organizerID) {
        userService.getByUserID(organizerID)
                .addOnSuccessListener(organizer -> {
                    if (organizer != null) {
                        organizerUsername.setText("Organizer Username: " + organizer.getUsername());
                        organizerEmail.setText("Organizer Email: " + organizer.getEmail());
                        if (organizer.getPhoneNumber() != null && !organizer.getPhoneNumber().isEmpty()) {
                            organizerPhone.setText("Organizer Phone: " + organizer.getPhoneNumber());
                        } else {
                            organizerPhone.setText("Organizer Phone: No phone number provided");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load organizer details", e);
                    Toast.makeText(getContext(), "Failed to load organizer details", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Checks the waitlist status for the current user and event,
     * updates the waitlist capacity UI and waitlist button state.
     */
    private void checkWaitlistStatus() {
        waitingListEntryService.getWaitlistSize(currentEvent.getEventID())
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) throw task.getException();
                    Long size = task.getResult();
                    currentEvent.setCurrentWaitingListSize(size.intValue());
                    Integer maxWaitListSize = currentEvent.getWaitlistCapacity();
                    String waitlistSizeText = (maxWaitListSize == null)
                            ? String.format("Waitlist capacity: %d/no limit", size)
                            : String.format("Waitlist capacity: %d/%d", size, maxWaitListSize);
                    waitlistCapacity.setText(waitlistSizeText);

                    return waitingListEntryService.getWaitlistEntries(currentEvent.getEventID());
                })
                .addOnSuccessListener(entries -> {
                    boolean alreadyJoined = entries.stream()
                            .anyMatch(entry -> entry.getUserID().equals(currentEntrantId));
                    updateWaitlistButton(alreadyJoined);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to check waitlist status", e);
                    Toast.makeText(getContext(), "Failed to check waitlist status", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Updates the waitlist button text and click listener based on whether the user
     * has already joined the waitlist.
     *
     * @param alreadyJoined True if the user is already on the waitlist
     */
    private void updateWaitlistButton(boolean alreadyJoined) {
        if (alreadyJoined) {
            waitlistButton.setText("Leave waitlist");
            waitlistButton.setOnClickListener(v -> leaveWaitlist());
        } else {
            waitlistButton.setText("Join Waitlist");
            waitlistButton.setOnClickListener(v -> joinWaitlist());
        }
    }

    /**
     * Adds the current user to the event waitlist.
     */
    private void joinWaitlist() {
        waitingListEntryService.join(currentEntrantId, currentEvent.getEventID())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Successfully joined waitlist", Toast.LENGTH_SHORT).show();
                    updateWaitlistButton(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to join waitlist", e);
                    Toast.makeText(getActivity(), "Failed to join waitlist", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Removes the current user from the event waitlist.
     */
    private void leaveWaitlist() {
        waitingListEntryService.leave(currentEntrantId, currentEvent.getEventID())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Successfully left waitlist", Toast.LENGTH_SHORT).show();
                    updateWaitlistButton(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to leave waitlist", e);
                    Toast.makeText(getActivity(), "Failed to leave waitlist", Toast.LENGTH_SHORT).show();
                });
    }
}
