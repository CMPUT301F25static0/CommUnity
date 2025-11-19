package com.example.community.Screens.OrganizerScreens;

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
import com.example.community.WaitingListEntryService;

public class OrganizerEventDescriptionFragment extends Fragment {

    public static final String TAG = "OrganizerEventDescriptionFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private Event currentEvent;
    private String currentOrganizerId;

    private WaitingListEntryService waitingListEntryService;
    private UserService userService;
    private EventService eventService;


    private TextView eventTitle, eventDescription, eventDates
            , registrationDates, capacity, waitlistCount, attendeeCount, invitedCount;
    private Button editButton, viewAttendeesButton, viewWaitlistButton, viewInvitedButton, backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View organizerEventDescriptionFragment = inflater.inflate(R.layout.organizer_event_description_page, container, false);
        return organizerEventDescriptionFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waitingListEntryService = new WaitingListEntryService();
        userService = new UserService();
        eventService = new EventService();
        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> currentOrganizerId = userId);

        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.eventDescription);
        eventDates = view.findViewById(R.id.eventDates);
        registrationDates = view.findViewById(R.id.registrationDates);
        capacity = view.findViewById(R.id.capacity);
        waitlistCount = view.findViewById(R.id.organizerWaitlistCount);
        attendeeCount = view.findViewById(R.id.organizerAttendeeCount);
        invitedCount = view.findViewById(R.id.organizerInvitedCount);

        editButton = view.findViewById(R.id.organizerEditEventButton);
        viewAttendeesButton = view.findViewById(R.id.viewAttendeesButton);
        viewWaitlistButton = view.findViewById(R.id.viewWaitlistButton);
        viewInvitedButton = view.findViewById(R.id.viewInvitedButton);
        backButton = view.findViewById(R.id.organizerEventDescriptionBackButton);

        loadEventDetails();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        editButton.setOnClickListener(v -> editEvent());
        viewAttendeesButton.setOnClickListener(v -> viewAttendeesList());
        viewWaitlistButton.setOnClickListener(v -> viewWaitlist());
        viewInvitedButton.setOnClickListener(v -> viewInvitedList());
    }

    private void loadEventDetails() {
        String eventId = getArguments().getString(ARG_EVENT_ID);
        if (eventId == null) {
            Toast.makeText(getContext(), "Invalid event", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
            return;
        }

        eventService.getEvent(eventId)
                .addOnSuccessListener(event -> {
                    currentEvent = event;
                    eventTitle.setText(event.getTitle());
                    eventDescription.setText(event.getDescription());
                    eventDates.setText(String.format("Event Dates: %s - %s",
                            event.getEventStartDate(), event.getEventEndDate()));
                    registrationDates.setText(String.format("Registration Period: %s - %s",
                            event.getRegistrationStart(), event.getEventEndDate()));
                    capacity.setText(String.format("Amount Attendees: %d/%d",
                            event.getCurrentCapacity(), event.getMaxCapacity()));
                    loadWaitlistCount();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load event details", e);
                    Toast.makeText(getContext(), "Failed to load event details", Toast.LENGTH_SHORT)
                            .show();
                });
    }

    private void loadWaitlistCount() {
        waitingListEntryService.getWaitlistSize(currentEvent.getEventID())
                .addOnSuccessListener(size -> {
                    currentEvent.setCurrentWaitingListSize(size.intValue());
                    Integer maxWaitListSize = currentEvent.getWaitlistCapacity();
                    String waitlistSizeText = (maxWaitListSize == null)
                            ? String.format("Waitlist: %d/no limit", size)
                            : String.format("Waitlist: %d/%d", size, maxWaitListSize);
                    waitlistCount.setText(waitlistSizeText);

                    return;
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist count", e);
                    Toast.makeText(getContext(), "Failed to load waitlist count", Toast.LENGTH_SHORT)
                            .show();
                });
    }

    private void editEvent() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Edit event not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void viewAttendeesList() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "View attendees not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void viewWaitlist() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "View waitlist not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void viewInvitedList() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "View invited not implemented yet", Toast.LENGTH_SHORT).show();
    }

}
