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

public class EntrantEventDescriptionFragment extends Fragment {

    public static final String TAG = "EventDescriptionFragment";

    private static final String ARG_EVENT_ID = "event_id";
    private Event currentEvent;
    private WaitingListEntryService waitingListEntryService;
    private UserService userService;
    private EventService eventService;
    private String currentEntrantId;

    private TextView eventTitle, eventDescription, eventDates
            , registrationDates, capacity, waitlistCapacity;
    private Button waitlistButton, backButton;

    public static EntrantEventDescriptionFragment newInstance(String eventId) {
        EntrantEventDescriptionFragment fragment = new EntrantEventDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View eventDescriptionFragment = inflater.inflate(R.layout.entrant_event_description, container, false);
        return eventDescriptionFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waitingListEntryService = new WaitingListEntryService();
        eventService = new EventService();
        userService = new UserService();
        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> currentEntrantId = userId);

        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.eventDescription);
        eventDates = view.findViewById(R.id.eventDates);
        registrationDates = view.findViewById(R.id.registrationDates);
        capacity = view.findViewById(R.id.capacity);
        waitlistCapacity = view.findViewById(R.id.waitlistCount);
        waitlistButton = view.findViewById(R.id.waitlistButton);
        backButton = view.findViewById(R.id.backButton);

        loadEventDetails();

        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(EntrantEventDescriptionFragment.this)
                    .navigateUp();
        });


    }

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
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load event details", e);
                    Toast.makeText(getContext(), "Failed to load event details", Toast.LENGTH_SHORT)
                            .show();
                });
    }

    private void setUpButtons() {
        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());
    }

    private void checkWaitlistStatus() {
        waitingListEntryService.getWaitlistSize(currentEvent.getEventID())
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
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
                    boolean alreadyJoined = false;
                    for (WaitingListEntry entry : entries) {
                        if (entry.getUserID().equals(currentEntrantId)) {
                            alreadyJoined = true;
                            break;
                        }
                    }
                    updateWaitlistButton(alreadyJoined);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to check waitlist status", e);
                    Toast.makeText(getContext(), "Failed to check waitlist status", Toast.LENGTH_SHORT)
                            .show();

        });

    }

    private void updateWaitlistButton(boolean alreadyJoined) {
        if (alreadyJoined) {
            waitlistButton.setText("Leave waitlist");
            waitlistButton.setOnClickListener(v -> leaveWaitlist());
        } else {
            waitlistButton.setText("Join Waitlist");
            waitlistButton.setOnClickListener(v -> joinWaitlist());
        }
    }

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
