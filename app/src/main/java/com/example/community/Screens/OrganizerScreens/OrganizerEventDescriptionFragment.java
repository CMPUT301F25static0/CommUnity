package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

/**
 * Fragment to display detailed information about an event for organizers.
 * Provides functionality to:
 * <ul>
 *     <li>Edit the event</li>
 *     <li>Upload a poster image</li>
 *     <li>View attendee, waitlist, invited, declined, and cancelled lists</li>
 *     <li>Run a lottery for limited spots</li>
 * </ul>
 */
public class OrganizerEventDescriptionFragment extends Fragment {

    /** Tag used for logging */
    public static final String TAG = "OrganizerEventDescriptionFragment";

    /** Argument key for passing event ID to this fragment */
    private static final String ARG_EVENT_ID = "event_id";

    /** Currently loaded event */
    private Event currentEvent;

    /** Service for managing waiting list entries */
    private WaitingListEntryService waitingListEntryService;

    /** Service for managing user data */
    private UserService userService;

    /** Service for managing events */
    private EventService eventService;

    /** UI components */
    private ImageView posterImageView;
    private TextView eventTitle, eventDescription, eventLocation, eventDates,
            registrationDates, capacity, organizerUsername, organizerEmail, organizerPhone,
            waitlistCount, attendeeCount, invitedCount;

    /** Buttons for event actions */
    private Button editButton, uploadPosterButton, viewAttendeesButton,
            viewWaitlistButton, viewInvitedButton, viewDeclinedButton,
            viewCancelledButton, runLotteryButton, backButton;

    /**
     * Inflates the fragment layout.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Parent view that fragment's UI should attach to
     * @param savedInstanceState Previously saved state of the fragment
     * @return Root View of the fragment layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_event_description_page, container, false);
    }

    /**
     * Initializes UI components, sets up services, loads event details,
     * and sets click listeners for all buttons.
     *
     * @param view Root view of the fragment
     * @param savedInstanceState Previously saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waitingListEntryService = new WaitingListEntryService();
        userService = new UserService();
        eventService = new EventService();

        posterImageView = view.findViewById(R.id.posterImageView);
        eventTitle = view.findViewById(R.id.eventTitle);
        eventDescription = view.findViewById(R.id.eventDescription);
        eventLocation = view.findViewById(R.id.eventLocation);
        eventDates = view.findViewById(R.id.eventDates);
        registrationDates = view.findViewById(R.id.registrationDates);
        capacity = view.findViewById(R.id.capacity);
        organizerUsername = view.findViewById(R.id.eventOrganizerName);
        organizerEmail = view.findViewById(R.id.eventOrganizerEmail);
        organizerPhone = view.findViewById(R.id.eventOrganizerPhone);
        waitlistCount = view.findViewById(R.id.organizerWaitlistCount);
        attendeeCount = view.findViewById(R.id.organizerAttendeeCount);
        invitedCount = view.findViewById(R.id.organizerInvitedCount);

        editButton = view.findViewById(R.id.organizerEditEventButton);
        uploadPosterButton = view.findViewById(R.id.uploadPosterButton);
        viewAttendeesButton = view.findViewById(R.id.viewAttendeesButton);
        viewWaitlistButton = view.findViewById(R.id.viewWaitlistButton);
        viewInvitedButton = view.findViewById(R.id.viewInvitedButton);
        viewCancelledButton = view.findViewById(R.id.viewCancelledButton);
        viewDeclinedButton = view.findViewById(R.id.viewDeclinedButton);
        runLotteryButton = view.findViewById(R.id.runLotteryButton);
        backButton = view.findViewById(R.id.organizerEventDescriptionBackButton);

        loadEventDetails();
        setUpClickListeners();
    }

    /**
     * Reloads event details when returning to this fragment.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (currentEvent != null) {
            loadEventDetails();
        }
    }

    /**
     * Sets click listeners for all buttons in the fragment.
     */
    private void setUpClickListeners() {
        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        editButton.setOnClickListener(v -> editEvent());
        uploadPosterButton.setOnClickListener(v -> uploadPoster());
        viewAttendeesButton.setOnClickListener(v -> viewAttendeesList());
        viewWaitlistButton.setOnClickListener(v -> viewWaitlist());
        viewInvitedButton.setOnClickListener(v -> viewInvitedList());
        viewCancelledButton.setOnClickListener(v -> viewCancelledList());
        viewDeclinedButton.setOnClickListener(v -> viewDeclinedList());
        runLotteryButton.setOnClickListener(v -> showLotteryConfirmationDialog());
    }

    /**
     * Loads event details from the EventService and updates the UI.
     * Fetches poster, organizer details, and waitlist count.
     */
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
                    eventLocation.setText("Event Location: " + event.getLocation());
                    eventDates.setText(String.format("Event Dates: %s - %s",
                            event.getEventStartDate(), event.getEventEndDate()));
                    registrationDates.setText(String.format("Registration Period: %s - %s",
                            event.getRegistrationStart(), event.getRegistrationEnd()));
                    capacity.setText(String.format("Amount Attendees: %d/%d",
                            event.getCurrentCapacity(), event.getMaxCapacity()));

                    loadPosterImage();
                    loadWaitlistCount();
                    loadOrganizerDetails(event.getOrganizerID());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load event details", e);
                    Toast.makeText(getContext(), "Failed to load event details", Toast.LENGTH_SHORT)
                            .show();
                });
    }

    /**
     * Loads the event poster image using Picasso.
     * Hides the ImageView if no poster is available.
     */
    private void loadPosterImage() {
        if (currentEvent == null) {
            posterImageView.setVisibility(View.GONE);
            return;
        }

        String posterURL = currentEvent.getPosterImageURL();
        if (posterURL != null && !posterURL.isEmpty()) {
            Picasso.get()
                    .load(posterURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(posterImageView);
            posterImageView.setVisibility(View.VISIBLE);
        } else {
            posterImageView.setVisibility(View.GONE);
        }
    }

    /**
     * Loads organizer details from the UserService and updates UI.
     *
     * @param organizerID The ID of the event organizer
     */
    private void loadOrganizerDetails(String organizerID) {
        userService.getByUserID(organizerID)
                .addOnSuccessListener(organizer -> {
                    if (organizer != null) {
                        organizerUsername.setText("Organizer Username: " + organizer.getUsername());
                        organizerEmail.setText("Organizer Email: " + organizer.getEmail());
                        organizerPhone.setText(
                                organizer.getPhoneNumber() != null && !organizer.getPhoneNumber().isEmpty()
                                        ? "Organizer Phone: " + organizer.getPhoneNumber()
                                        : "Organizer Phone: No phone number provided"
                        );
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load organizer details", e);
                    Toast.makeText(getContext(), "Failed to load organizer details", Toast.LENGTH_SHORT)
                            .show();
                });
    }

    /**
     * Loads the current event's waitlist count and updates the UI.
     */
    private void loadWaitlistCount() {
        waitingListEntryService.getWaitlistSize(currentEvent.getEventID())
                .addOnSuccessListener(size -> {
                    currentEvent.setCurrentWaitingListSize(size.intValue());
                    Integer maxWaitListSize = currentEvent.getWaitlistCapacity();
                    String waitlistSizeText = (maxWaitListSize == null)
                            ? String.format("Waitlist: %d/no limit", size)
                            : String.format("Waitlist: %d/%d", size, maxWaitListSize);
                    waitlistCount.setText(waitlistSizeText);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load waitlist count", e);
                    Toast.makeText(getContext(), "Failed to load waitlist count", Toast.LENGTH_SHORT)
                            .show();
                });
    }

    /**
     * Navigates to the event editing fragment with current event details.
     */
    private void editEvent() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle args = new Bundle();
        args.putString("event_id", currentEvent.getEventID());
        args.putString("event_name", currentEvent.getTitle());
        args.putString("event_description", currentEvent.getDescription());
        args.putString("event_location", currentEvent.getLocation());
        args.putString("event_start_date", currentEvent.getEventStartDate());
        args.putString("event_end_date", currentEvent.getEventEndDate());
        args.putString("reg_start", currentEvent.getRegistrationStart());
        args.putString("reg_end", currentEvent.getRegistrationEnd());
        args.putInt("max_participants", currentEvent.getMaxCapacity());
        if (currentEvent.getWaitlistCapacity() != null) {
            args.putInt("waiting_list_size", currentEvent.getWaitlistCapacity());
        }
        args.putBoolean("is_edit_mode", true);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_OrganizerEventDescriptionFragment_to_CreateEventFragment, args);
    }

    /**
     * Navigates to the fragment for uploading a poster image.
     */
    private void uploadPoster() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle args = new Bundle();
        args.putString("event_id", currentEvent.getEventID());

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_OrganizerEventDescriptionFragment_to_OrganizerPosterUploadFragment, args);
    }

    /**
     * Opens a dialog showing the event waitlist.
     */
    private void viewWaitlist() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        OrganizerEventUserListFragment fragment =
                OrganizerEventUserListFragment.newInstance(currentEvent.getEventID(), "waitlist");
        fragment.show(getChildFragmentManager(), "waitlist_list");
    }

    /**
     * Opens a dialog showing the list of attendees.
     */
    private void viewAttendeesList() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        OrganizerEventUserListFragment fragment =
                OrganizerEventUserListFragment.newInstance(currentEvent.getEventID(), "attendees");
        fragment.show(getChildFragmentManager(), "attendees_list");
    }

    /** Opens a dialog showing the list of invited users */
    private void viewInvitedList() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        OrganizerEventUserListFragment fragment =
                OrganizerEventUserListFragment.newInstance(currentEvent.getEventID(), "invited");
        fragment.show(getChildFragmentManager(), "invited_list");
    }

    /** Opens a dialog showing the list of cancelled users */
    private void viewCancelledList() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        OrganizerEventUserListFragment fragment =
                OrganizerEventUserListFragment.newInstance(currentEvent.getEventID(), "cancelled");
        fragment.show(getChildFragmentManager(), "cancelled_list");
    }

    /** Opens a dialog showing the list of declined users */
    private void viewDeclinedList() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        OrganizerEventUserListFragment fragment =
                OrganizerEventUserListFragment.newInstance(currentEvent.getEventID(), "declined");
        fragment.show(getChildFragmentManager(), "declined_list");
    }

    /**
     * Opens a dialog to confirm running a lottery for the event.
     */
    private void showLotteryConfirmationDialog() {
        if (currentEvent == null) {
            Toast.makeText(getContext(), "Event not loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        LotteryConfirmationDialogFragment fragment =
                LotteryConfirmationDialogFragment.newInstance(currentEvent.getEventID());
        fragment.show(getChildFragmentManager(), "lottery_confirmation");
    }
}
